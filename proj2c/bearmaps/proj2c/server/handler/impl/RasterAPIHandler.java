package bearmaps.proj2c.server.handler.impl;

import bearmaps.proj2c.AugmentedStreetMapGraph;
import bearmaps.proj2c.server.handler.APIRouteHandler;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import bearmaps.proj2c.utils.Constants;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static bearmaps.proj2c.utils.Constants.*;

/**
 * Handles requests from the web browser for map images. These images
 * will be rastered into one large image to be displayed to the user.
 * @author rahul, Josh Hug, _________
 */
public class RasterAPIHandler extends APIRouteHandler<Map<String, Double>, Map<String, Object>> {



    /**
     * Each raster request to the server will have the following parameters
     * as keys in the params map accessible by,
     * i.e., params.get("ullat") inside RasterAPIHandler.processRequest(). <br>
     * ullat : upper left corner latitude, <br> ullon : upper left corner longitude, <br>
     * lrlat : lower right corner latitude,<br> lrlon : lower right corner longitude <br>
     * w : user viewport window width in pixels,<br> h : user viewport height in pixels.
     **/
    private static final String[] REQUIRED_RASTER_REQUEST_PARAMS = {"ullat", "ullon", "lrlat",
            "lrlon", "w", "h"};

    /**
     * The result of rastering must be a map containing all of the
     * fields listed in the comments for RasterAPIHandler.processRequest.
     **/
    private static final String[] REQUIRED_RASTER_RESULT_PARAMS = {"render_grid", "raster_ul_lon",
            "raster_ul_lat", "raster_lr_lon", "raster_lr_lat", "depth", "query_success"};


    @Override
    protected Map<String, Double> parseRequestParams(Request request) {
        return getRequestParams(request, REQUIRED_RASTER_REQUEST_PARAMS);
    }

    /**
     * Takes a user query and finds the grid of images that best matches the query. These
     * images will be combined into one big image (rastered) by the front end. <br>
     *
     *     The grid of images must obey the following properties, where image in the
     *     grid is referred to as a "tile".
     *     <ul>
     *         <li>The tiles collected must cover the most longitudinal distance per pixel
     *         (LonDPP) possible, while still covering less than or equal to the amount of
     *         longitudinal distance per pixel in the query box for the user viewport size. </li>
     *         <li>Contains all tiles that intersect the query bounding box that fulfill the
     *         above condition.</li>
     *         <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     *     </ul>
     *
     * @param requestParams Map of the HTTP GET request's query parameters - the query box and
     *               the user viewport width and height.
     *
     * @param response : Not used by this function. You may ignore.
     * @return A map of results for the front end as specified: <br>
     * "render_grid"   : String[][], the files to display. <br>
     * "raster_ul_lon" : Number, the bounding upper left longitude of the rastered image. <br>
     * "raster_ul_lat" : Number, the bounding upper left latitude of the rastered image. <br>
     * "raster_lr_lon" : Number, the bounding lower right longitude of the rastered image. <br>
     * "raster_lr_lat" : Number, the bounding lower right latitude of the rastered image. <br>
     * "depth"         : Number, the depth of the nodes of the rastered image;
     *                    can also be interpreted as the length of the numbers in the image
     *                    string. <br>
     * "query_success" : Boolean, whether the query was able to successfully complete; don't
     *                    forget to set this to true on success! <br>
     */
    @Override
    public Map<String, Object> processRequest(Map<String, Double> requestParams, Response response) {
        System.out.println("yo, wanna know the parameters given by the web browser? They are:");
        System.out.println(requestParams);
        Map<String, Object> results = new HashMap<>();
//        System.out.println("Since you haven't implemented RasterAPIHandler.processRequest, nothing is displayed in "
//                + "your browser.");

        Map<String, Double> rootBounding = getRootBounding();
        if (!isIntersect(requestParams, rootBounding)) {
            return queryFail();
        }

        String[][] render_grid;
        double raster_ul_lon, raster_ul_lat, raster_lr_lon, raster_lr_lat;
        int depth;
        boolean query_success;

        double xDist = Math.abs(requestParams.get("ullon") - requestParams.get("lrlon"));
        double lonDPP = xDist / requestParams.get("w");
        depth = getDepth(lonDPP);
        render_grid = getRenderGrid(requestParams, depth);
        String firstFile = render_grid[0][0];
        String lastFile = render_grid[render_grid.length - 1][render_grid[0].length - 1];
        Map<String, Double> firstBounding = computeFileBounding(firstFile);
        Map<String, Double> lastBounding = computeFileBounding(lastFile);
        raster_ul_lon = firstBounding.get("ullon");
        raster_ul_lat = firstBounding.get("ullat");
        raster_lr_lon = lastBounding.get("lrlon");
        raster_lr_lat = lastBounding.get("lrlat");
        query_success = true;

        results.put("depth", depth);
        results.put("query_success", query_success);
        results.put("raster_ul_lon", raster_ul_lon);
        results.put("raster_ul_lat", raster_ul_lat);
        results.put("raster_lr_lon", raster_lr_lon);
        results.put("raster_lr_lat", raster_lr_lat);
        results.put("render_grid", render_grid);
        return results;
    }

    private Map<String, Double> getRootBounding() {
        Map<String, Double> rootBounding = new HashMap<>();
        rootBounding.put("ullon", ROOT_ULLON);
        rootBounding.put("ullat", ROOT_ULLAT);
        rootBounding.put("lrlon", ROOT_LRLON);
        rootBounding.put("lrlat", ROOT_LRLAT);
        return rootBounding;
    }

    private boolean isIntersect(Map<String, Double> boundingsA, Map<String, Double> boundingsB) {
        Double minLonDistA = boundingsA.get("ullon");
        Double maxLonDistA = boundingsA.get("lrlon");
        Double minLatDistA = boundingsA.get("lrlat");
        Double maxLatDistA = boundingsA.get("ullat");

        Double minLonDistB = boundingsB.get("ullon");
        Double maxLonDistB = boundingsB.get("lrlon");
        Double minLatDistB = boundingsB.get("lrlat");
        Double maxLatDistB = boundingsB.get("ullat");

        return !(minLatDistA > maxLatDistB) && !(minLatDistB > maxLatDistA) && !(minLonDistA > maxLonDistB) && !(minLonDistB > maxLonDistA);
    }

    private String[][] getFileGridByDepth(int depth) {
        int figure1DCounts = (int) Math.pow(2, depth);
        String[][] grid = new String[figure1DCounts][figure1DCounts];
        for (int i=0; i!=figure1DCounts; i++) {
            for (int j=0; j!=figure1DCounts; j++) {
                StringBuilder sb = new StringBuilder();
                sb.append("d");
                sb.append(depth);
                sb.append("_x");
                sb.append(j);
                sb.append("_y");
                sb.append(i);
                sb.append(".png");
                grid[i][j] = sb.toString();
            }
        }
        return grid;
    }

    public static void main(String[] args) {
        RasterAPIHandler test = new RasterAPIHandler();
        Map<String, Double> m = test.computeFileBounding("d4_x10_y3.png");
        System.out.println(m.get("ullon"));
        System.out.println(m.get("ullat"));
        System.out.println(m.get("lrlon"));
        System.out.println(m.get("lrlat"));
    }

    private String[][] getRenderGrid(Map<String, Double> requestParams, int depth) {
        String[][] fileGrid = getFileGridByDepth(depth);
        List<List<String>> resultGrid = new ArrayList<>();

        int figure1DCounts = (int) Math.pow(2, depth);
        for (int i=0; i!=figure1DCounts; i++) {
            List<String> subList = new ArrayList<>();
            for (int j = 0; j != figure1DCounts; j++) {
                String filename = fileGrid[i][j];
                Map<String, Double> fileBounding = computeFileBounding(filename);
                if (isIntersect(requestParams, fileBounding)) {
                    subList.add(filename);
                }
            }
            if (!subList.isEmpty()) {
                resultGrid.add(subList);
            }
        }
        String[][] resultArray = new String[resultGrid.size()][];
        int i = 0;
        for (List<String> subList: resultGrid) {
            resultArray[i++] = subList.toArray(new String[0]);
        }
        return resultArray;
    }

    private Map<String, Double> computeFileBounding(String filename) {
        Pattern p = Pattern.compile("\\d+");
        Matcher m = p.matcher(filename);
        m.find();
        int depth = Integer.parseInt(m.group());
        m.find();
        int x = Integer.parseInt(m.group());
        m.find();
        int y = Integer.parseInt(m.group());

        int figure1DCounts = (int) Math.pow(2, depth);
        double deltaLonDist = Math.abs(ROOT_LRLON - ROOT_ULLON) / figure1DCounts;
        double deltaLatDist = Math.abs(ROOT_LRLAT - ROOT_ULLAT) / figure1DCounts;
        double ullon = ROOT_ULLON + deltaLonDist*x;
        double lrlon = ullon + deltaLonDist;
        double ullat = ROOT_ULLAT - deltaLatDist*y;
        double lrlat = ullat - deltaLatDist;
        Map<String, Double> result = new HashMap<>();
        result.put("ullon", ullon);
        result.put("lrlon", lrlon);
        result.put("ullat", ullat);
        result.put("lrlat", lrlat);
        return result;
    }


    private int getDepth(double goal) {
        int depth = 0;
        double lonParam = Math.abs(ROOT_LRLON - ROOT_ULLON) / 256;
        double lonDPP = lonParam / Math.pow(2, depth);
        while (depth != 7) {
            if (lonDPP > goal) {
                lonDPP /= 2;
                depth++;
            } else {
                break;
            }
        }
        return depth;
    }

    @Override
    protected Object buildJsonResponse(Map<String, Object> result) {
        boolean rasterSuccess = validateRasteredImgParams(result);

        if (rasterSuccess) {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            writeImagesToOutputStream(result, os);
            String encodedImage = Base64.getEncoder().encodeToString(os.toByteArray());
            result.put("b64_encoded_image_data", encodedImage);
        }
        return super.buildJsonResponse(result);
    }

    private Map<String, Object> queryFail() {
        Map<String, Object> results = new HashMap<>();
        results.put("render_grid", null);
        results.put("raster_ul_lon", 0);
        results.put("raster_ul_lat", 0);
        results.put("raster_lr_lon", 0);
        results.put("raster_lr_lat", 0);
        results.put("depth", 0);
        results.put("query_success", false);
        return results;
    }

    /**
     * Validates that Rasterer has returned a result that can be rendered.
     * @param rip : Parameters provided by the rasterer
     */
    private boolean validateRasteredImgParams(Map<String, Object> rip) {
        for (String p : REQUIRED_RASTER_RESULT_PARAMS) {
            if (!rip.containsKey(p)) {
                System.out.println("Your rastering result is missing the " + p + " field.");
                return false;
            }
        }
        if (rip.containsKey("query_success")) {
            boolean success = (boolean) rip.get("query_success");
            if (!success) {
                System.out.println("query_success was reported as a failure");
                return false;
            }
        }
        return true;
    }

    /**
     * Writes the images corresponding to rasteredImgParams to the output stream.
     * In Spring 2016, students had to do this on their own, but in 2017,
     * we made this into provided code since it was just a bit too low level.
     */
    private void writeImagesToOutputStream(Map<String, Object> rasteredImageParams,
                                                  ByteArrayOutputStream os) {
        String[][] renderGrid = (String[][]) rasteredImageParams.get("render_grid");
        int numVertTiles = renderGrid.length;
        int numHorizTiles = renderGrid[0].length;

        BufferedImage img = new BufferedImage(numHorizTiles * Constants.TILE_SIZE,
                numVertTiles * Constants.TILE_SIZE, BufferedImage.TYPE_INT_RGB);
        Graphics graphic = img.getGraphics();
        int x = 0, y = 0;

        for (int r = 0; r < numVertTiles; r += 1) {
            for (int c = 0; c < numHorizTiles; c += 1) {
                graphic.drawImage(getImage(Constants.IMG_ROOT + renderGrid[r][c]), x, y, null);
                x += Constants.TILE_SIZE;
                if (x >= img.getWidth()) {
                    x = 0;
                    y += Constants.TILE_SIZE;
                }
            }
        }

        /* If there is a route, draw it. */
        double ullon = (double) rasteredImageParams.get("raster_ul_lon"); //tiles.get(0).ulp;
        double ullat = (double) rasteredImageParams.get("raster_ul_lat"); //tiles.get(0).ulp;
        double lrlon = (double) rasteredImageParams.get("raster_lr_lon"); //tiles.get(0).ulp;
        double lrlat = (double) rasteredImageParams.get("raster_lr_lat"); //tiles.get(0).ulp;

        final double wdpp = (lrlon - ullon) / img.getWidth();
        final double hdpp = (ullat - lrlat) / img.getHeight();
        AugmentedStreetMapGraph graph = SEMANTIC_STREET_GRAPH;
        List<Long> route = ROUTE_LIST;

        if (route != null && !route.isEmpty()) {
            Graphics2D g2d = (Graphics2D) graphic;
            g2d.setColor(Constants.ROUTE_STROKE_COLOR);
            g2d.setStroke(new BasicStroke(Constants.ROUTE_STROKE_WIDTH_PX,
                    BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            route.stream().reduce((v, w) -> {
                g2d.drawLine((int) ((graph.lon(v) - ullon) * (1 / wdpp)),
                        (int) ((ullat - graph.lat(v)) * (1 / hdpp)),
                        (int) ((graph.lon(w) - ullon) * (1 / wdpp)),
                        (int) ((ullat - graph.lat(w)) * (1 / hdpp)));
                return w;
            });
        }

        rasteredImageParams.put("raster_width", img.getWidth());
        rasteredImageParams.put("raster_height", img.getHeight());

        try {
            ImageIO.write(img, "png", os);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private BufferedImage getImage(String imgPath) {
        BufferedImage tileImg = null;
        if (tileImg == null) {
            try {
//                File in = new File(imgPath);
//                tileImg = ImageIO.read(in);
                tileImg = ImageIO.read(Thread.currentThread().getContextClassLoader().getResource(imgPath));
            } catch (IOException | NullPointerException e) {
                e.printStackTrace();
            }
        }
        return tileImg;
    }
}
