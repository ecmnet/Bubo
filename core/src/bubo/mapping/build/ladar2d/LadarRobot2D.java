/*
 * Copyright (c) 2013-2014, Peter Abeles. All Rights Reserved.
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

package bubo.mapping.build.ladar2d;

import bubo.desc.RobotDescription;
import bubo.desc.sensors.lrf2d.Lrf2dParam;
import georegression.struct.se.Se2_F64;

/**
 * @author Peter Abeles
 */
public class LadarRobot2D {

	RobotDescription desc;

	public LadarRobot2D() {

	}

	public void setLadarParam(Se2_F64 ladarToRobot,
							  Lrf2dParam param) {

	}

	public void setRobotPosition(double x, double y, double yaw) {

	}

	public Se2_F64 getLadarToWorld() {
		return null;
	}
}
