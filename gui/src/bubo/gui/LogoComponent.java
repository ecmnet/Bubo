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

package bubo.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Used for displaying logos
 *
 * @author Peter Abeles
 */
public class LogoComponent extends JComponent {

	private BufferedImage logo;
	private double logoTranslucent = 0.0;
	private double logoScale = 1.0;

	public LogoComponent(BufferedImage logo) {
		this.logo = logo;

		Dimension d = new Dimension(logo.getWidth(), logo.getHeight());

		setSize(d);
		setPreferredSize(d);
		setMinimumSize(d);
	}

	public BufferedImage getLogo() {
		return logo;
	}

	@Override
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, y, width, height);

	}

	@Override
	public void paintComponent(Graphics g) {

		Graphics2D g2 = (Graphics2D) g;

		float alpha = 0.75f;
		int type = AlphaComposite.SRC_OVER;

		AlphaComposite composite =
				AlphaComposite.getInstance(type, alpha);

		g2.setComposite(composite);
		g2.drawImage(logo, 0, 0, null);

//        Dimension d = getSize();
//
//        g.setColor(Color.RED);
//        g.fillRect(0,0,d.width,d.height);
	}
}
