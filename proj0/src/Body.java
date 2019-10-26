public class Body {
    public double xxPos;
    public double yyPos;
    public double xxVel;
    public double yyVel;
    public double mass;
    public static double G = 6.67 * Math.pow(10, -11);
    public String imgFileName;

    public Body(double xP, double yP, double xV, double yV, double m, String img){
        this.xxPos = xP;
        this.yyPos = yP;
        this.xxVel = xV;
        this.yyVel = yV;
        this.mass = m;
        this.imgFileName = img;
    }

    public Body(Body b){
        this.xxPos = b.xxPos;
        this.yyPos = b.yyPos;
        this.xxVel = b.xxVel;
        this.yyVel = b.yyVel;
        this.mass = b.mass;
        this.imgFileName = b.imgFileName;
    }

    public double calcDistance(Body b) {
        return Math.sqrt(Math.pow((this.xxPos - b.xxPos), 2) + Math.pow((this.yyPos - b.yyPos), 2));
    }

    public double calcForceExertedBy(Body b){
        double distance = this.calcDistance(b);
        return this.mass * b.mass * G / Math.pow(distance, 2);
    }

    public double calcForceExertedByX(Body b) {
        double distance = this.calcDistance(b);
        double distanceX = b.xxPos - this.xxPos;
        double force = this.calcForceExertedBy(b);
        return distanceX / distance * force;
    }

    public double calcForceExertedByY(Body b) {
        double distance = this.calcDistance(b);
        double distanceY = b.yyPos - this.yyPos;
        double force = this.calcForceExertedBy(b);
        return distanceY / distance * force;
    }

    public double calcForceExertedByX(Body[] bs) {
        double forceX = 0;
        for (Body b: bs){
            if (this.equals(b)) continue;
            forceX += this.calcForceExertedByX(b);
        }
        return forceX;
    }

    public double calcForceExertedByY(Body[] bs) {
        double forceY = 0;
        for (Body b: bs){
            if (this.equals(b)) continue;
            forceY += this.calcForceExertedByY(b);
        }
        return forceY;
    }

    public void update(double dt, double fX, double fY) {
        double xa = fX / this.mass;
        double ya = fY / this.mass;
        this.xxVel += dt*xa;
        this.yyVel += dt*ya;
        this.xxPos += dt*this.xxVel;
        this.yyPos += dt*this.yyVel;
    }

    public void draw() {
        String imgFilePath = "./images/" + this.imgFileName;
        StdDraw.picture(this.xxPos, this.yyPos, imgFilePath);
    }

}
