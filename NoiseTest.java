import Noise.SimplexNoise;
import java.io.*;

class NoiseTest {
    public static void main(String args[]) {
        if(args.length > 0){
            try {
                FileWriter fstream = new FileWriter(args[0]);
                BufferedWriter out = new BufferedWriter(fstream);
                int width = 500, height = 500;
                double init = 500;

                out.write("P3\n# simplex noise\n" + Integer.toString(width) + " " + Integer.toString(height) + "\n256\n");
                for(int y = 0; y < 500; y++){
                    for(int x = 0; x < 500; x++){
                        double val = 0;
                        double max = 0;

                        for(int is = 0; is < 16; is++){
                            double s = 1/Math.pow(2,is);
                            max += s;
                            val += s*SimplexNoise.Noise((((float)x) + 50*Math.sin(((float)y)/100+val))/(init*s), (float)y/(init*s));
                        }
                        val /= max;

                        // val = Math.sin(((float)y)/100)*val;
                        // val = (val > 0)?val:-1;
                        // val = Math.abs(val);
                        // val = Math.abs(Math.sin(100*val));
                        val = Math.sin(100*val);

                        int r = 0, g = 0, b = 0;
                        r = g = b = (val<0)?0:255;
                        /* if(val < 0.1){
                           val *= 10;
                           r = (int)(128 - 64*val);
                           g = (int)(256 - 128*val*val);
                           b = (int)(256*val*val);
                           } else {
                           val = (val - 0.1)/0.9;
                           r = (int)(256 - 256*val);
                           g = 0;
                           b = (int)(64 - 65*Math.sqrt(val));
                           } */

                        out.write(Integer.toString(r) + " " + Integer.toString(g) + " " + Integer.toString(b) + "\n");
                    }
                    out.write("\n");
                }
                out.close();
            } catch (Exception e) {
                System.out.println("lol, error.");
            }
        }
    }
}
