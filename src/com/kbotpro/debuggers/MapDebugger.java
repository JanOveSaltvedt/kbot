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

/*
 * Copyright © 2010 Jan Ove Saltvedt.
 * All rights reserved.
 */

package com.kbotpro.debuggers;

import com.kbotpro.hooks.Client;
import com.kbotpro.hooks.MapData;
import com.kbotpro.hooks.TileData;
import com.kbotpro.scriptsystem.events.PaintEventListener;
import com.kbotpro.scriptsystem.fetch.Objects;
import com.kbotpro.scriptsystem.runnable.Debugger;
import com.kbotpro.scriptsystem.wrappers.PhysicalObject;
import com.kbotpro.scriptsystem.wrappers.Tile;
import com.kbotpro.ui.FieldWatcher;
import org.apache.log4j.Logger;

import javax.imageio.ImageIO;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Jan Ove Saltvedt
 * Date: Feb 23, 2010
 * Time: 5:46:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class MapDebugger extends Debugger implements PaintEventListener {
    private boolean shallRun = false;
    private Tile currentTile;
    private UI ui;

    /**
     * Gets the name shown in the debugs menu
     *
     * @return String containing name
     */
    @Override
    public String getName() {
        return "Map Debugger";
    }

    /**
     * Is called before the debugger starts to check if it can run.
     *
     * @return Returns a boolean indicating if the service can be started or not
     */
    @Override
    public boolean canStart() {
        return true;
    }

    /**
     * Is called right before the run() gets called
     */
    @Override
    public void onStart() {
        shallRun = true;
        currentTile = getLocation();
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                ui = new UI();
                ui.xSpinner.setValue(currentTile.getX());
                ui.ySpinner.setValue(currentTile.getY());

                ui.myPosButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        currentTile = getLocation();
                        ui.xSpinner.setValue(currentTile.getX());
                        ui.ySpinner.setValue(currentTile.getY());
                    }
                });

                ui.xSpinner.addChangeListener(new ChangeListener() {
                    public void stateChanged(ChangeEvent e) {
                        currentTile = new Tile((Integer) ui.xSpinner.getValue(), (Integer) ui.ySpinner.getValue());
                    }
                });
                ui.ySpinner.addChangeListener(new ChangeListener() {
                    public void stateChanged(ChangeEvent e) {
                        currentTile = new Tile((Integer) ui.xSpinner.getValue(), (Integer) ui.ySpinner.getValue());
                    }
                });
                ui.reflectButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        final Client client = getClient();
                        int x = currentTile.getX() - client.getBaseX();
                        int y = currentTile.getY() - client.getBaseY();

                        final TileData[][][] datas = client.getTileDataArray();
                        if (datas == null || datas.length < 1 || datas[0] == null) {
                            return;
                        }
                        TileData data = datas[0][x][y];
                        new FieldWatcher(data).setVisible(true);

                    }
                });

                ui.createMapButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        createMap();
                    }
                });
                ui.setVisible(true);
            }
        });
    }

    /**
     * Is called to pause debugger.
     */
    @Override
    public void pause() {
    }

    /**
     * Is called to stop the debugger.
     * The debugger is than added to the cleanup queue and thread will be force killed if not deleted within 10 seconds.
     */
    @Override
    public void stop() {
        shallRun = false;
        ui.dispose();
    }

    /**
     * You should implement the main loop here.
     */
    @Override
    public void run() {
        while (shallRun) {
            dumpData();
            sleep(1000);
        }
    }

    private void dumpData() {
        final Client client = getClient();
        final MapData[] datas = client.getMapDataArray();
        final int currentPlane = client.getCurrentPlane();
        for(int plane = 0; plane < datas.length; plane++){
            final String mapDataPath = "map_data/" + currentPlane;
            final File mapDataFolder = new File(mapDataPath);
            if (!mapDataFolder.exists()) {
                if (!mapDataFolder.mkdirs()) {
                    return;
                }
            }

            int baseX = client.getBaseX();
            int baseY = client.getBaseY();

            final String mapPath = mapDataPath + "/" + baseX + "x" + baseY + "L-"+plane+".png";
            if (new File(mapPath).exists()) {
                return;
            }

            final BufferedImage image = generateMap(plane);

            try {
                ImageIO.write(image, "png", new File(mapPath));
            } catch (IOException e) {
                Logger.getRootLogger().error("Exception: ", e);  //To change body of catch statement use File | Settings | File Templates.
            }

            log("Wrote map to " + mapPath);
        }
    }

    private void createMap() {
        BufferedImage bufferedImage = generateMap(getClient().getCurrentPlane());

        try {
            ImageIO.write(bufferedImage, "png", new File("map.out.png"));
        } catch (IOException e) {
            Logger.getRootLogger().error("Exception: ", e);  //To change body of catch statement use File | Settings | File Templates.
        }


    }

    private BufferedImage generateMap(int plane) {
        final Client client = getClient();
        MapData mapData = client.getMapDataArray()[plane];
        final int[][] tileDataArray = mapData.getTileData();
        final int width = 104 * 8;
        final int height = 104 * 8;
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = bufferedImage.createGraphics();
        g.setColor(Color.white);
        g.fillRect(0, 0, width, height);
        g.setColor(Color.black);

        for (int x = 1; x < 105; x++) {
            for (int y = 1; y < 105; y++) {
                PhysicalObject[] physicalObjects = objects.getObjectsAt(x, y, Objects.MASK_INTERACTIVE);
                if (physicalObjects != null && physicalObjects.length > 0) {
                    boolean foundDoor = false;
                    for (PhysicalObject object : physicalObjects) {

                    }
                }
                int tileData = tileDataArray[x][y];
                if ((tileData & FULL_BLOCK) != 0) {
                    int screenX = (x - 1) * 8;
                    int screenY = (y - 1) * 8;
                    screenY = height - screenY - 8;
                    g.fillRect(screenX, screenY, 8, 8);
                }
                if ((tileData & BLOCKED_0_NORTH) != 0) {
                    int screenX = (x - 1) * 8;
                    int screenY = (y - 1) * 8;
                    screenY = height - screenY - 8;
                    g.drawLine(screenX, screenY, screenX + 8, screenY);
                }
                if ((tileData & BLOCKED_0_EAST) != 0) {
                    int screenX = (x - 1) * 8;
                    int screenY = (y - 1) * 8;
                    screenY = height - screenY - 8;
                    g.drawLine(screenX + 8, screenY, screenX + 8, screenY + 8);
                }
                if ((tileData & BLOCKED_0_SOUTH) != 0) {
                    int screenX = (x - 1) * 8;
                    int screenY = (y - 1) * 8;
                    screenY = height - screenY - 8;
                    g.drawLine(screenX, screenY + 8, screenX + 8, screenY + 8);
                }
                if ((tileData & BLOCKED_0_WEST) != 0) {
                    int screenX = (x - 1) * 8;
                    int screenY = (y - 1) * 8;
                    screenY = height - screenY - 8;
                    g.drawLine(screenX, screenY, screenX, screenY + 8);
                }

                if ((tileData & BLOCKED_0_NORTH_EAST) != 0) {
                    int screenX = (x - 1) * 8;
                    int screenY = (y - 1) * 8;
                    screenY = height - screenY - 8;
                    g.fillRect(screenX+7, screenY-1, 2, 2);
                }
                if ((tileData & BLOCKED_0_SOUTH_EAST) != 0) {
                    int screenX = (x - 1) * 8;
                    int screenY = (y - 1) * 8;
                    screenY = height - screenY - 8;
                    g.fillRect(screenX+7, screenY+7, 2, 2);
                }
                if ((tileData & BLOCKED_0_SOUTH_WEST) != 0) {
                    int screenX = (x - 1) * 8;
                    int screenY = (y - 1) * 8;
                    screenY = height - screenY - 8;
                    g.fillRect(screenX-1, screenY+7, 2, 2);
                }
                if ((tileData & BLOCKED_0_NORTH_WEST) != 0) {
                    int screenX = (x - 1) * 8;
                    int screenY = (y - 1) * 8;
                    screenY = height - screenY - 8;
                    g.fillRect(screenX-1, screenY-1, 2, 2);
                }

            }
        }
        return bufferedImage;
    }

    private final int BLOCKING_OBJECT = 0x100;

    private final int BLOCKED_0_NORTH = 0x2;
    private final int BLOCKED_0_NORTH_EAST = 0x4;
    private final int BLOCKED_0_EAST = 0x8;
    private final int BLOCKED_0_SOUTH_EAST = 0x10;
    private final int BLOCKED_0_SOUTH = 0x20;
    private final int BLOCKED_0_SOUTH_WEST = 0x40;
    private final int BLOCKED_0_WEST = 0x80;
    private final int BLOCKED_0_NORTH_WEST = 0x200;

    private final int BLOCKED_1_NORTH = 0x400;
    private final int BLOCKED_1_NORTH_EAST = 0x800;
    private final int BLOCKED_1_EAST = 0x1000;
    private final int BLOCKED_1_SOUTH_EAST = 0x2000;
    private final int BLOCKED_1_SOUTH = 0x4000;
    private final int BLOCKED_1_SOUTH_WEST = 0x8000;
    private final int BLOCKED_1_WEST = 0x10000;
    private final int BLOCKED_1_NORTH_WEST = 0x40000;

    private final int BLOCKED_2_NORTH = 0x800000;
    private final int BLOCKED_2_NORTH_EAST = 0x1000000;
    private final int BLOCKED_2_EAST = 0x2000000;
    private final int BLOCKED_2_SOUTH_EAST = 0x4000000;
    private final int BLOCKED_2_SOUTH = 0x8000000;
    private final int BLOCKED_2_SOUTH_WEST = 0x10000000;
    private final int BLOCKED_2_WEST = 0x20000000;
    private final int BLOCKED_2_NORTH_WEST = 0x80000000;


    private final int BLOCK_0 = 0x100;
    private final int BLOCK_1 = 0x40000;
    private final int BLOCK_2 = 0x200000;
    private final int BLOCK_3 = 0x40000000;

    private final int FULL_BLOCK = /*BLOCK_0 | */BLOCK_1 | BLOCK_2 | BLOCK_3;

    public void onRepaint(Graphics g) {
        if (currentTile == null) {
            return;
        }
        final int baseX = getClient().getBaseX();
        final int baseY = getClient().getBaseY();
        g.setColor(new Color(Color.blue.getRed(), Color.blue.getGreen(), Color.blue.getBlue(), 120));

        int rsStartX = currentTile.getRegionalX(baseX);
        int rsStartY = currentTile.getRegionalY(baseY);
        int rsEndX = rsStartX + 256;
        int rsEndY = rsStartY + 256;

        Polygon polygon = new Polygon();
        Point point = calculations.worldToScreen(rsStartX, rsStartY, 0);
        Point p2 = calculations.worldToScreen(rsStartX-256, rsStartY-256, 0);
        polygon.addPoint(p2.x, p2.y);
        p2 = calculations.worldToScreen(rsStartX-256, rsStartY+256, 0);
        polygon.addPoint(p2.x, p2.y);
        p2 = calculations.worldToScreen(rsStartX+256, rsStartY+256, 0);
        polygon.addPoint(p2.x, p2.y);
        p2 = calculations.worldToScreen(rsStartX+256, rsStartY-256, 0);
        polygon.addPoint(p2.x, p2.y);

        g.fillPolygon(polygon);

        g.setColor(Color.pink);
        final Point mmPoint = calculations.tileToMinimap(currentTile);
        g.fillOval(mmPoint.x - 2, mmPoint.y - 2, 4, 4);

        try {
            g.setColor(Color.WHITE);
            final Client client = getClient();
            MapData mapData = client.getMapDataArray()[client.getCurrentPlane()];
            int x = currentTile.getX() - baseX + 1;
            int y = currentTile.getY() - baseY + 1;


            int tileData = mapData.getTileData()[x][y];
            g.drawString("TileData (i): " + tileData, 20, 60);
            String binaryTileData = Integer.toString(tileData, 2);
            g.drawString("TileData (b): " + binaryTileData, 20, 80);
            g.drawString("Dumping data is on", 20, 100);
            int flagNum = 0;

            int newTileData = tileData;
            if ((newTileData & BLOCKED_0_NORTH) != 0) {
                g.drawString("Blocked edge north", 20, 140 + 20 * flagNum);
                flagNum++;
                newTileData = newTileData ^ BLOCKED_0_NORTH;
            }
            if ((newTileData & BLOCKED_0_NORTH_EAST) != 0) {
                g.drawString("Blocked edge north east", 20, 140 + 20 * flagNum);
                flagNum++;
                newTileData = newTileData ^ BLOCKED_0_NORTH_EAST;
            }
            if ((newTileData & BLOCKED_0_EAST) != 0) {
                g.drawString("Blocked edge east", 20, 140 + 20 * flagNum);
                flagNum++;
                newTileData = newTileData ^ BLOCKED_0_EAST;
            }
            if ((newTileData & BLOCKED_0_SOUTH_EAST) != 0) {
                g.drawString("Blocked edge south east", 20, 140 + 20 * flagNum);
                flagNum++;
                newTileData = newTileData ^ BLOCKED_0_SOUTH_EAST;
            }
            if ((newTileData & BLOCKED_0_SOUTH) != 0) {
                g.drawString("Blocked edge south", 20, 140 + 20 * flagNum);
                flagNum++;
                newTileData = newTileData ^ BLOCKED_0_SOUTH;
            }
            if ((newTileData & BLOCKED_0_SOUTH_WEST) != 0) {
                g.drawString("Blocked edge south west", 20, 140 + 20 * flagNum);
                flagNum++;
                newTileData = newTileData ^ BLOCKED_0_SOUTH_WEST;
            }
            if ((newTileData & BLOCKED_0_WEST) != 0) {
                g.drawString("Blocked edge west", 20, 140 + 20 * flagNum);
                flagNum++;
                newTileData = newTileData ^ BLOCKED_0_WEST;
            }
            if ((newTileData & BLOCKED_0_NORTH_WEST) != 0) {
                g.drawString("Blocked north west", 20, 140 + 20 * flagNum);
                flagNum++;
                newTileData = newTileData ^ BLOCKED_0_NORTH_WEST;
            }

            if ((newTileData & (BLOCKED_1_NORTH | BLOCKED_2_NORTH)) != 0) {
                g.drawString("Can not walk north", 20, 140 + 20 * flagNum);
                flagNum++;
                newTileData = newTileData ^ (BLOCKED_1_NORTH | BLOCKED_2_NORTH);
            }
            if ((newTileData & (BLOCKED_1_NORTH_EAST | BLOCKED_2_NORTH_EAST)) != 0) {
                g.drawString("Can not walk north east", 20, 140 + 20 * flagNum);
                flagNum++;
                newTileData = newTileData ^ (BLOCKED_1_NORTH_EAST | BLOCKED_2_NORTH_EAST);
            }

            if ((newTileData & (BLOCKED_1_EAST | BLOCKED_2_EAST)) != 0) {
                g.drawString("Can not walk east", 20, 140 + 20 * flagNum);
                flagNum++;
                newTileData = newTileData ^ (BLOCKED_1_EAST | BLOCKED_2_EAST);
            }

            if ((newTileData & (BLOCKED_1_SOUTH | BLOCKED_2_SOUTH)) != 0) {
                g.drawString("Can not walk south", 20, 140 + 20 * flagNum);
                flagNum++;
                newTileData = newTileData ^ (BLOCKED_1_SOUTH | BLOCKED_2_SOUTH);
            }
            if ((newTileData & (BLOCKED_1_SOUTH_EAST | BLOCKED_2_SOUTH_EAST)) != 0) {
                g.drawString("Can not walk south east", 20, 140 + 20 * flagNum);
                flagNum++;
                newTileData = newTileData ^ (BLOCKED_1_SOUTH_EAST | BLOCKED_2_SOUTH_EAST);
            }

            if ((newTileData & (BLOCKED_1_SOUTH_WEST | BLOCKED_2_WEST)) != 0) {
                g.drawString("Can not walk south west", 20, 140 + 20 * flagNum);
                flagNum++;
                newTileData = newTileData ^ (BLOCKED_1_SOUTH_WEST | BLOCKED_2_SOUTH_WEST);
            }

            if ((newTileData & (BLOCKED_1_WEST | BLOCKED_2_WEST)) != 0) {
                g.drawString("Can not walk west", 20, 140 + 20 * flagNum);
                flagNum++;
                newTileData = newTileData ^ (BLOCKED_1_WEST | BLOCKED_2_WEST);
            }

            if ((newTileData & (BLOCKED_1_NORTH_WEST | BLOCKED_2_NORTH_WEST)) != 0) {
                g.drawString("Can not walk west", 20, 140 + 20 * flagNum);
                flagNum++;
                newTileData = newTileData ^ (BLOCKED_1_NORTH_WEST | BLOCKED_2_NORTH_WEST);
            }

            if ((newTileData & (FULL_BLOCK)) != 0) {
                g.drawString("Tile is blocked", 20, 140 + 20 * flagNum);
                flagNum++;
                newTileData = newTileData ^ (FULL_BLOCK);
            }


            int pos = 0;
            binaryTileData = Integer.toString(newTileData, 2);
            for (int i = binaryTileData.length() - 1; i >= 0; i--, pos++) {
                char c = binaryTileData.charAt(i);
                if (c != '1') {
                    continue;
                }

                int hex = 1 << pos;
                g.drawString("Flag" + pos + "(0x" + Integer.toString(hex, 16) + ") is true", 20, 140 + 20 * flagNum);
                flagNum++;

            }

        } catch (Throwable throwable) {
            g.drawString(throwable.getMessage(), 20, 100);
        }
    }
}
