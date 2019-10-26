



public class NBody {
    public static int N;

    public static double readRadius(String filePath){
        In in = new In(filePath);
        N = in.readInt();
        double radius = in.readDouble();
        return radius;
    }


    public static Body[] readBodies(String filePath){
        In in = new In(filePath);
        N = in.readInt();
        Body[] bs = new Body[N];
        double radius = in.readDouble();
        in.readLine();
        for (int index=0; index<N; index++){
            String[] line = in.readLine().trim().split("\\s+");
            double xxPos = Double.parseDouble(line[0]);
            double yyPos = Double.parseDouble(line[1]);
            double xxVel = Double.parseDouble(line[2]);
            double yyVel = Double.parseDouble(line[3]);
            double mass = Double.parseDouble(line[4]);
            String imgFileName = line[5];
            Body b = new Body(xxPos, yyPos, xxVel, yyVel, mass, imgFileName);
            bs[index] = b;
            }
        return bs;
    }

    public static void main(String[] args){
        double T = Double.parseDouble(args[0]);
        double dt = Double.parseDouble(args[1]);
        String filename = args[2];
        Body[] bs = NBody.readBodies(filename);
        double radius = NBody.readRadius(filename);
        StdDraw.enableDoubleBuffering();
        StdDraw.setScale(-radius, radius);
        StdDraw.clear();
        for (double time = 0.0; time <= T; time+=dt) {
            double[] xForces = new double[N];
            double[] yForces = new double[N];
            int index = 0;
            for (Body b: bs){
                double xForce = b.calcForceExertedByX(bs);
                double yForce = b.calcForceExertedByY(bs);
                xForces[index] = xForce;
                yForces[index] = yForce;
                b.update(dt, xForce, yForce);
                index++;
            }

            StdDraw.picture(0, 0, "./images/starfield.jpg");
            for (Body b: bs){
                b.draw();
            }
            StdDraw.show();
            StdDraw.pause(10);
        }
        StdOut.printf("%d\n", N);
        StdOut.printf("%.2e\n", radius);
        for (int i=0; i<N; i++){
            StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
                    bs[i].xxPos, bs[i].yyPos, bs[i].xxVel, bs[i].yyVel, bs[i].mass, bs[i].imgFileName);
        }


    }
}
