/*	
	Copyright 2012 Jan Ove Saltvedt
	
	This file is part of KBot.

    KBot is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    KBot is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with KBot.  If not, see <http://www.gnu.org/licenses/>.
	
*/



package com.kbotpro.scriptsystem.graphics;

import com.kbotpro.scriptsystem.wrappers.Model;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderableImage;
import java.text.AttributedCharacterIterator;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Scott
 * Date: Dec 19, 2009
 * Time: 7:05:33 AM
 * To change this template use File | Settings | File Templates.
 */
public class KGraphics extends Graphics2D {
	private Graphics2D proxyGraphics;

	public KGraphics(Graphics2D proxyGraphics) {
		this.proxyGraphics = proxyGraphics;
	}

	@Override
	public void draw(Shape s) {
		proxyGraphics.draw(s);
	}

	@Override
	public boolean drawImage(Image img, AffineTransform xform, ImageObserver obs) {
		return proxyGraphics.drawImage(img, xform, obs); /// and so on
	}

	@Override
	public void drawImage(BufferedImage img, BufferedImageOp op, int x, int y) {
		proxyGraphics.drawImage(img, op, x, y);
	}

	@Override
	public void drawRenderedImage(RenderedImage img, AffineTransform xform) {
		proxyGraphics.drawRenderedImage(img, xform);
	}

	@Override
	public void drawRenderableImage(RenderableImage img, AffineTransform xform) {
		proxyGraphics.drawRenderableImage(img, xform);
	}

	@Override
	public void drawString(String str, int x, int y) {
		proxyGraphics.drawString(str, x, y);
	}

	@Override
	public void drawString(String str, float x, float y) {
		proxyGraphics.drawString(str, x, y);
	}

	@Override
	public void drawString(AttributedCharacterIterator iterator, int x, int y) {
		proxyGraphics.drawString(iterator, x, y);
	}

	@Override
	public boolean drawImage(Image img, int x, int y, ImageObserver observer) {
		return proxyGraphics.drawImage(img, x, y, observer);
	}

	@Override
	public boolean drawImage(Image img, int x, int y, int width, int height, ImageObserver observer) {
		return proxyGraphics.drawImage(img, x, y, width, height, observer);
	}

	@Override
	public boolean drawImage(Image img, int x, int y, Color bgcolor, ImageObserver observer) {
		return proxyGraphics.drawImage(img, x, y, bgcolor, observer);
	}

	@Override
	public boolean drawImage(Image img, int x, int y, int width, int height, Color bgcolor, ImageObserver observer) {
		return proxyGraphics.drawImage(img, x, y, width, height, bgcolor, observer);
	}

	@Override
	public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, ImageObserver observer) {
		return proxyGraphics.drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, observer);
	}

	@Override
	public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, Color bgcolor, ImageObserver observer) {
		return proxyGraphics.drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, bgcolor, observer);
	}

	@Override
	public void dispose() {
		proxyGraphics.dispose();
	}

	@Override
	public void drawString(AttributedCharacterIterator iterator, float x, float y) {
		proxyGraphics.drawString(iterator, x, y);
	}

	@Override
	public void drawGlyphVector(GlyphVector g, float x, float y) {
		proxyGraphics.drawGlyphVector(g, x, y);
	}

	@Override
	public void fill(Shape s) {
		proxyGraphics.fill(s);
	}

	@Override
	public boolean hit(Rectangle rect, Shape s, boolean onStroke) {
		return proxyGraphics.hit(rect, s, onStroke);
	}

	@Override
	public GraphicsConfiguration getDeviceConfiguration() {
		return proxyGraphics.getDeviceConfiguration();
	}

	@Override
	public void setComposite(Composite comp) {
		proxyGraphics.setComposite(comp);
	}

	@Override
	public void setPaint(Paint paint) {
		proxyGraphics.setPaint(paint);
	}

	@Override
	public void setStroke(Stroke s) {
		proxyGraphics.setStroke(s);
	}

	@Override
	public void setRenderingHint(RenderingHints.Key hintKey, Object hintValue) {
		proxyGraphics.setRenderingHint(hintKey, hintValue);
	}

	@Override
	public Object getRenderingHint(RenderingHints.Key hintKey) {
		return proxyGraphics.getRenderingHint(hintKey);
	}

	@Override
	public void setRenderingHints(Map<?, ?> hints) {
		proxyGraphics.setRenderingHints(hints);
	}

	@Override
	public void addRenderingHints(Map<?, ?> hints) {
		proxyGraphics.addRenderingHints(hints);
	}

	@Override
	public RenderingHints getRenderingHints() {
		return proxyGraphics.getRenderingHints();
	}

	@Override
	public Graphics create() {
		return proxyGraphics.create();
	}

	@Override
	public void translate(int x, int y) {
		proxyGraphics.translate(x, y);
	}

	@Override
	public Color getColor() {
		return proxyGraphics.getColor();
	}

	@Override
	public void setColor(Color c) {
		proxyGraphics.setColor(c);
	}

	@Override
	public void setPaintMode() {
		proxyGraphics.setPaintMode();
	}

	@Override
	public void setXORMode(Color c1) {
		proxyGraphics.setXORMode(c1);
	}

	@Override
	public Font getFont() {
		return proxyGraphics.getFont();
	}

	@Override
	public void setFont(Font font) {
		proxyGraphics.setFont(font);
	}

	@Override
	public FontMetrics getFontMetrics(Font f) {
		return proxyGraphics.getFontMetrics(f);
	}

	@Override
	public Rectangle getClipBounds() {
		return proxyGraphics.getClipBounds();
	}

	@Override
	public void clipRect(int x, int y, int width, int height) {
		proxyGraphics.clipRect(x, y, width, height);
	}

	@Override
	public void setClip(int x, int y, int width, int height) {
		proxyGraphics.setClip(x, y, width, height);
	}

	@Override
	public Shape getClip() {
		return proxyGraphics.getClip();
	}

	@Override
	public void setClip(Shape clip) {
		proxyGraphics.setClip(clip);
	}

	@Override
	public void copyArea(int x, int y, int width, int height, int dx, int dy) {
		proxyGraphics.copyArea(x, y, width, height, dx, dy);
	}

	@Override
	public void drawLine(int x1, int y1, int x2, int y2) {
		proxyGraphics.drawLine(x1, y1, x2, y2);
	}

	@Override
	public void fillRect(int x, int y, int width, int height) {
		proxyGraphics.fillRect(x, y, width, height);
	}

	@Override
	public void clearRect(int x, int y, int width, int height) {
		proxyGraphics.clearRect(x, y, width, height);
	}

	@Override
	public void drawRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
		proxyGraphics.drawRoundRect(x, y, width, height, arcWidth, arcHeight);
	}

	@Override
	public void fillRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
		proxyGraphics.fillRoundRect(x, y, width, height, arcWidth, arcHeight);
	}

	@Override
	public void drawOval(int x, int y, int width, int height) {
		proxyGraphics.drawOval(x, y, width, height);
	}

	@Override
	public void fillOval(int x, int y, int width, int height) {
		proxyGraphics.fillOval(x, y, width, height);
	}

	@Override
	public void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
		proxyGraphics.drawArc(x, y, width, height, startAngle, arcAngle);
	}

	@Override
	public void fillArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
		proxyGraphics.fillArc(x, y, width, height, startAngle, arcAngle);
	}

	@Override
	public void drawPolyline(int[] xPoints, int[] yPoints, int nPoints) {
		proxyGraphics.drawPolyline(xPoints, yPoints, nPoints);
	}

	@Override
	public void drawPolygon(int[] xPoints, int[] yPoints, int nPoints) {
		proxyGraphics.drawPolygon(xPoints, yPoints, nPoints);
	}

	@Override
	public void fillPolygon(int[] xPoints, int[] yPoints, int nPoints) {
		proxyGraphics.fillPolygon(xPoints, yPoints, nPoints);
	}

	@Override
	public void translate(double tx, double ty) {
		proxyGraphics.translate(tx, ty);
	}

	@Override
	public void rotate(double theta) {
		proxyGraphics.rotate(theta);
	}

	@Override
	public void rotate(double theta, double x, double y) {
		proxyGraphics.rotate(theta, x, y);
	}

	@Override
	public void scale(double sx, double sy) {
		proxyGraphics.scale(sx, sy);
	}

	@Override
	public void shear(double shx, double shy) {
		proxyGraphics.shear(shx, shy);
	}

	@Override
	public void transform(AffineTransform Tx) {
		proxyGraphics.transform(Tx);
	}

	@Override
	public void setTransform(AffineTransform Tx) {
		proxyGraphics.setTransform(Tx);
	}

	@Override
	public AffineTransform getTransform() {
		return proxyGraphics.getTransform();
	}

	@Override
	public Paint getPaint() {
		return proxyGraphics.getPaint();
	}

	@Override
	public Composite getComposite() {
		return proxyGraphics.getComposite();
	}

	@Override
	public void setBackground(Color color) {
		proxyGraphics.setBackground(color);
	}

	@Override
	public Color getBackground() {
		return proxyGraphics.getBackground();
	}

	@Override
	public Stroke getStroke() {
		return proxyGraphics.getStroke();
	}

	@Override
	public void clip(Shape s) {
		proxyGraphics.clip(s);
	}

	@Override
	public FontRenderContext getFontRenderContext() {
		return proxyGraphics.getFontRenderContext();
	}

	/**
	 * Draws the models wireframe
	 *
	 * @param model The model to draw to
	 */
	public void drawModelWireframe(Model model) {
		model.drawWireframe(proxyGraphics);
	}

	/**
	 * Draws the models bounding box
	 *
	 * @param model The model to draw to
	 */
	public void drawModelBoundingBox(Model model) {
		model.drawBoundingBox(proxyGraphics);
	}

	/**
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param percentage 0 through 100  
	 * @param topDown should draw from the top down or left to right
	 */
	public void drawSquarePercentageBar(int x, int y, int width, int height, double percentage, boolean topDown) {
		Color c = getColor();          
		int drawPercentage = (int) (((percentage > 100 ? 100 : percentage) / 100) * width);
		if (topDown)        
			fillRect(x, y, (width > height ? drawPercentage : width), (height >= width ? drawPercentage : height));
		else {
			int factorX = (width > height ? width - drawPercentage : 0);
			int factorY = (height >= width ? height - drawPercentage : 0);
			fillRect(x + factorX, y + factorY, width - factorX, height - factorY);
		}
		setColor(Color.BLACK);
		drawRect(x, y, width, height);
		setColor(c);
	}

	/**
	 * @param rect points to do to
	 * @param percentage 0 through 100  
	 * @param topDown should draw from the top down or left to right
	 */
	public void drawSquarePercentageBar(Rectangle rect, double percentage, boolean topDown) {
		drawSquarePercentageBar(rect.x, rect.y, rect.width, rect.height, percentage, topDown);
	}


	/**
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param percentage 0 through 100
	 * @param topDown should draw from the top down or left to right
	 */
	public void drawRoundedPercentageBar(int x, int y, int width, int height, double percentage, boolean topDown) {
		int archWidth = width > height ? 10 : width;
		int archHeight = height >= width ? 10 : height;
		Color c = getColor();
		int drawPercentage = (int) (((percentage > 100 ? 100 : percentage) / 100) * (height >= width ? height : height));
		if (topDown)  
			fillRoundRect(x, y, (width > height ? drawPercentage : width), (height >= width ? drawPercentage : height), archWidth, archHeight);
		else {
			int factorX = (width > height ? width - drawPercentage : 0);
			int factorY = (height >= width ? height - drawPercentage : 0);
			fillRoundRect(x + factorX, y + factorY, width - factorX, height - factorY, archWidth, archHeight);
		}
		setColor(Color.BLACK);
		drawRoundRect(x, y, width, height, archWidth, archHeight);     
		setColor(c);
	}

	/**
	 * @param rect points to do to
	 * @param percentage 0 through 100  
	 * @param topDown should draw from the top down or left to right
	 */
	public void drawRoundedPercentageBar(Rectangle rect, double percentage, boolean topDown) {
		drawRoundedPercentageBar(rect.x, rect.y, rect.width, rect.height, percentage, topDown);
	}

	/**
	 * Fills the model with. Slow!
	 *
	 * @param model The model to draw to
	 */
	public void fillModel(Model model) {
		model.fillWireFrame(proxyGraphics);
	}
}
