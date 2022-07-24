public class Imaginary {
    private double x;
    private double y;

    private double r;
    private double theta;

    public Imaginary(double a, double b, boolean polar){

        if(!polar){
            this.x = a;
            this.y = b;

            updatePolar();
        }

        else
        {
            this.r = a;
            this.theta = b;

            updateCartesian();
        }
    }

    private void updatePolar(){
        r = Math.sqrt(x*x + y*y);
        theta = Math.atan2(y,x);
    }

    private void updateCartesian(){
        fixTheta();
        x = r * Math.cos(theta);
        y = r * Math.sin(theta);
    }

    private void fixTheta(){
        //Fixes angle going over 2PI [360º]
        this.theta = this.theta % 2*Math.PI;

        if(this.theta < 0){
            this.theta += 2*Math.PI;
        }
    }

    public void add(Imaginary i){
        this.x += i.x;
        this.y += i.y;

        updatePolar();
    }

    public static Imaginary add(Imaginary a, Imaginary b){
        Imaginary result = new Imaginary(a.x + b.x,a.x + b.x,false);
        result.updatePolar();
        return result;
    }

    public void subtract(Imaginary i){
        this.x -= i.x;
        this.y -= i.y;

        updatePolar();
    }

    public static Imaginary subtract(Imaginary a, Imaginary b){
        Imaginary result = new Imaginary(a.x - b.x,a.x - b.x,false);
        result.updatePolar();
        return result;
    }

    public void multiply(Imaginary i){
        this.r *= i.r;
        this.theta += i.theta;

        updateCartesian();
    }

    public static Imaginary multiply(Imaginary a, Imaginary b){
        Imaginary result = new Imaginary(0,0,false);

        result.r = a.r * b.r;
        result.theta = a.theta + b.theta;

        result.updateCartesian();
        return result;
    }

    public void divide(Imaginary i){
        this.r /= i.r;
        this.theta -= i.theta;

        updateCartesian();
    }

    public static Imaginary divide(Imaginary a, Imaginary b){
        Imaginary result = new Imaginary(0,0, false);

        result.r = a.r / b.r;
        result.theta = a.theta - b.theta;

        result.updateCartesian();
        return result;
    }

    public double getX(){
        return this.x;
    }

    public double getY(){
        return this.y;
    }

    public double getR(){
        return this.r;
    }

    public double getTheta(){
        return this.theta;
    }

    public void printImaginary(){
        System.out.println("Cartesian: " + "(" + this.x + ", " + this.y + ")" + "  Polar: " + this.r + "∠" + this.theta);
    }
}
