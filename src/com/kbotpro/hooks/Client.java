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



package com.kbotpro.hooks;

import com.kbotpro.interfaces.ClientCallback;

import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.KeyListener;

/**
 * Created by IntelliJ IDEA.
 * User: Jan Ove Saltvedt
 * Date: Oct 17, 2009
 * Time: 12:11:51 PM
 * To change this template use File | Settings | File Templates.
 */
public interface Client {
    public Player getMyPlayer();
    public Player[] getPlayers();
    public NPC[] getNpcs();
    public Canvas getCanvas();
    public Mouse getMouse();
    public int getCameraCurveX();
    public int getCameraCurveY();
    public int getCameraX();
    public int getCameraY();
    public int getCameraZ();
    public int getBaseX();
    public int getBaseY();
    public int getCurrentPlane();
    public RenderVars getGameRenderVars();
    public Renderer getGameRenderer();
    public int getMenuX();
    public int getMenuY();
    public int getMenuWidth();
    public int getMenuHeight();
    public int getMenuOptionsCount();
    public TileData[][][] getTileDataArray();
    public Rectangle[] getInterfaceBounds();
    public IComponent[][] getIComponentArray();
    public int getGameState();
    public int getLoopCycle();

    public int[] getLevels();
    public int[] getExperiences();
    public int[] getMaxLevels();
    public int[] getMaxExperiences();

    public boolean[] getValidInterfaceArray();

    public Plane[] getPlaneArray();

    public byte[][][] getGroundSettingsArray();

    public void setCallback(ClientCallback clientCallback);

    public NodeList getMenuNodeList();

    public boolean isMenuOpen();

    public int[] getSettingsArray();

    public boolean[] getVisibleIComponents();

    public MouseListener getMouseListener();
    public MouseMotionListener getMouseMotionListener();
    public KeyListener getKeyListener();
    public int getMainUIInterfaceIndex();

    public float getCompassAngle();
    public int getMinimapSetting();
    public int getMinimapOffset();
    public int getMinimapScale();

    public boolean isScreenMenuItemSelected();
    public ViewSettings getViewSettings();

    public NodeCache getGroundObjectCache();

    public MapData[] getMapDataArray();

    public int getDestX();
    public int getDestY();
    public boolean isDestSet();

    public NPCNode[] getNPCNodes();
    public NodeCache getNPCNodeCache();

    public String getLoadingString();
}
