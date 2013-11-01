/*
 * Copyright (c) 2013, Peter Abeles. All Rights Reserved.
 *
 * This file is part of Project BUBO.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package bubo.ptcloud.alg;

import georegression.struct.point.Point3D_F64;
import org.ddogleg.struct.FastQueue;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

/**
 * @author Peter Abeles
 */
public class TestApproximateSurfaceNormals {

	Random rand = new Random(234234);

	/**
	 * Give it a simple plane and see if it produces the expected results
	 */
	@Test
	public void checkUsingAPlane() {

		double maxDistance = 0.4;
		int numNeighbors = 5;

		List<Point3D_F64> cloud = new ArrayList<Point3D_F64>();

		for( int i = 0; i < 50; i++ ) {
			for( int j = 0; j < 40; j++ ) {
				cloud.add( new Point3D_F64(j*0.1,i*0.1,-4));
			}
		}

		FastQueue<PointVectorNN> output = new FastQueue<PointVectorNN>(PointVectorNN.class,false);

		ApproximateSurfaceNormals alg = new ApproximateSurfaceNormals(numNeighbors,maxDistance,numNeighbors,maxDistance);

		alg.process(cloud,output);

		assertEquals(cloud.size(),output.size());

		for( int i = 0; i < cloud.size(); i++ ) {
			PointVectorNN pv = output.get(i);

			// see if the normal is valid
			assertEquals(0,pv.normal.x,1e-8);
			assertEquals(0,pv.normal.y,1e-8);
			assertEquals(1,Math.abs(pv.normal.z),1e-8);

			// see if it's neighbors are close by
			assertEquals(numNeighbors,pv.neighbors.size);
			for( int j = 0; j < pv.neighbors.size; j++ ) {
				double d = pv.neighbors.get(j).p.distance(pv.p);
				assertTrue(d <= maxDistance);

				// the point should not be a neighbor to itself
				assertTrue( Math.abs(d) > 1e-8 );
			}
		}
	}

	/**
	 * Makes sure the output vectors are normalized to one.  If no normal was found then it should be set to all zeros.
	 *
	 * Also checks to see that the index is correctly setup
	 */
	@Test
	public void checkVectorNormalizedToOne_index() {
		List<Point3D_F64> cloud = new ArrayList<Point3D_F64>();

		for( int i = 0; i < 200; i++ ) {
			double x = 3*(rand.nextDouble()-0.5);
			double y = 3*(rand.nextDouble()-0.5);
			double z = 3*(rand.nextDouble()-0.5);

			cloud.add( new Point3D_F64(x,y,z) );
		}

		FastQueue<PointVectorNN> output = new FastQueue<PointVectorNN>(PointVectorNN.class,false);

		ApproximateSurfaceNormals alg = new ApproximateSurfaceNormals(8,0.4,8,0.4);

		alg.process(cloud,output);

		assertEquals(cloud.size(),output.size());

		int numNorm = 0;
		int numZero = 0;
		for( int i = 0; i < cloud.size(); i++ ) {
			PointVectorNN pv = output.get(i);

			// check index
			assertEquals(i,pv.index);

			// check normal
			double n = pv.normal.norm();

			if( n == 0 ) {
				numZero++;
			} else {
				assertEquals(1,pv.normal.norm(),1e-8);
				numNorm++;
			}
		}

		assertTrue(numZero>0);
		assertTrue(numNorm>0);
	}

	/**
	 * If more neighbors are requested than points for use in plane estimation, then make sure only
	 * the closest ones are used.
	 */
	@Test
	public void useClosestNeighborsForPlane() {
		List<Point3D_F64> cloud = new ArrayList<Point3D_F64>();

		// give it points on the plane
		cloud.add(new Point3D_F64(0,0,0));
		cloud.add(new Point3D_F64(1,0,0));
		cloud.add(new Point3D_F64(0,1,0));
		cloud.add(new Point3D_F64(-1,-1,0));

		// now give it some points way off the plane
		cloud.add(new Point3D_F64(0,0,100));
		cloud.add(new Point3D_F64(1,0,-100));

		FastQueue<PointVectorNN> output = new FastQueue<PointVectorNN>(PointVectorNN.class,false);

		ApproximateSurfaceNormals alg = new ApproximateSurfaceNormals(4,10000,8,10000);

		alg.process(cloud,output);

		assertEquals(6,output.size);

		// on the first pass the first for should have good normals
		for( int i = 0; i < 4; i++ ) {
			PointVectorNN pv = output.data[i];
			assertEquals(5,pv.neighbors.size);
			assertEquals(0,pv.normal.x,1e-8);
			assertEquals(0,pv.normal.y,1e-8);
			assertEquals(1,Math.abs(pv.normal.z),1e-8);
		}

		output.reset();
		alg = new ApproximateSurfaceNormals(8,10000,8,10000);

		alg.process(cloud,output);
		assertEquals(6,output.size);

		// the second pass they should be messed up
		for( int i = 0; i < 4; i++ ) {
			PointVectorNN pv = output.data[i];
			assertEquals(5,pv.neighbors.size);
			assertFalse(Math.abs(pv.normal.x) < 1e-8);
			assertFalse(Math.abs(pv.normal.y) < 1e-8);
			assertFalse(Math.abs(Math.abs(pv.normal.z)-1) < 1e-8);
		}
	}

	/**
	 * Makes sure that the plane specific maximum distance is being honored
	 */
	@Test
	public void checkPlaneMaximumDistance() {
		List<Point3D_F64> cloud = new ArrayList<Point3D_F64>();

		// give it points on the plane
		cloud.add(new Point3D_F64(0,0,0));
		cloud.add(new Point3D_F64(1,0,0));
		cloud.add(new Point3D_F64(0,1,0));
		cloud.add(new Point3D_F64(-1,-1,0));

		// now give it some points way off the plane
		cloud.add(new Point3D_F64(0,0,100));
		cloud.add(new Point3D_F64(1,0,-100));

		FastQueue<PointVectorNN> output = new FastQueue<PointVectorNN>(PointVectorNN.class,false);

		ApproximateSurfaceNormals alg = new ApproximateSurfaceNormals(8,5,8,10000);

		alg.process(cloud,output);

		assertEquals(6,output.size);

		// on the first pass the first for should have good normals
		for( int i = 0; i < 4; i++ ) {
			PointVectorNN pv = output.data[i];
			// all the points should be in the neighbor list
			assertEquals(5,pv.neighbors.size);
			// however, only a subset of the points will be used to compute the normal.  If that is the case
			// then the normal will be pointed upwards
			assertEquals(0,pv.normal.x,1e-8);
			assertEquals(0,pv.normal.y,1e-8);
			assertEquals(1,Math.abs(pv.normal.z),1e-8);
		}
	}

	/**
	 * Make sure everything works when it is called multiple times
	 */
	@Test
	public void multipleCalls() {
		List<Point3D_F64> cloud = new ArrayList<Point3D_F64>();

		for( int i = 0; i < 200; i++ ) {
			double x = 3*(rand.nextDouble()-0.5);
			double y = 3*(rand.nextDouble()-0.5);
			double z = 3*(rand.nextDouble()-0.5);

			cloud.add( new Point3D_F64(x,y,z) );
		}

		FastQueue<PointVectorNN> output = new FastQueue<PointVectorNN>(PointVectorNN.class,false);

		ApproximateSurfaceNormals alg = new ApproximateSurfaceNormals(8,0.4,8,0.4);

		alg.process(cloud,output);

		assertEquals(cloud.size(),output.size());

		int numNorm = 0;
		int numZero = 0;
		for( int i = 0; i < cloud.size(); i++ ) {
			PointVectorNN pv = output.get(i);

			double n = pv.normal.norm();

			if( n == 0 ) {
				numZero++;
			} else {
				numNorm++;
			}
		}

		assertTrue(numZero>0);
		assertTrue(numNorm>0);

		output.reset();
		alg.process(cloud,output);

		assertEquals(cloud.size(),output.size());

		int numNorm2 = 0;
		int numZero2 = 0;
		for( int i = 0; i < cloud.size(); i++ ) {
			PointVectorNN pv = output.get(i);

			double n = pv.normal.norm();

			if( n == 0 ) {
				numZero2++;
			} else {
				numNorm2++;
			}
		}

		assertTrue(numZero == numZero2 );
		assertTrue(numNorm == numNorm2);
	}

}
