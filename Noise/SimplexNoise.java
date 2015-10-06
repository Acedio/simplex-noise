package Noise;

public class SimplexNoise {
    private static final int perms[] = { 151, 221, 181, 35, 178, 167, 130, 105, 62, 185, 160, 240, 229, 38, 113, 
        184, 223, 163, 143, 21, 179, 235, 59, 82, 57, 252, 140, 16, 84, 243, 
        204, 236, 104, 171, 28, 48, 75, 254, 11, 127, 69, 155, 246, 120, 37, 
         50, 212, 154, 237, 66, 251, 100, 6, 108, 157, 145, 225, 248, 36, 125, 
        220, 116, 3, 177, 96, 164, 43, 42, 176, 114, 56, 9, 63, 67, 95, 
        186, 138, 77, 207, 214, 198, 201, 26, 133, 8, 129, 18, 242, 233, 180, 
        131, 58, 126, 226, 136, 78, 53, 122, 161, 19, 215, 12, 255, 90, 202, 
        216, 227, 32, 238, 203, 83, 31, 134, 115, 205, 188, 224, 141, 119, 91, 
         10, 14, 199, 51, 86, 30, 137, 47, 74, 4, 187, 97, 68, 44, 70, 
        217, 117, 27, 211, 22, 150, 20, 250, 183, 85, 72, 80, 112, 147, 65, 
        118, 71, 144, 174, 247, 61, 213, 189, 244, 234, 102, 170, 239, 55, 249, 
         81, 230, 1, 13, 149, 166, 40, 89, 64, 109, 54, 103, 111, 92, 76, 
        195, 15, 128, 24, 23, 169, 245, 79, 165, 5, 222, 106, 39, 219, 192, 
        191, 33, 172, 88, 123, 190, 99, 208, 228, 209, 194, 241, 156, 41, 182, 
          2, 231, 107, 101, 124, 94, 46, 175, 93, 17, 87, 193, 49, 110, 162, 
         25, 142, 29, 232, 159, 168, 52, 158, 121, 197, 200, 0, 253, 153, 206, 
        132, 73, 173, 218, 146, 98, 45, 7, 135, 139, 34, 60, 152, 148, 196, 210};

    private static final double [][] grads = {{-0.4844741980417009, -0.8748055506407414},
        {0.30763591385380995, -0.9515041484446252},
        {0.966496112719775, 0.25668124999221104},
        {0.8850083824919798, 0.4655750883788024},
        {0.6868099889963177, 0.7268370099374948},
        {0.9348680342453562, 0.3549954345427887},
        {-0.4977912648980709, -0.8672968676244476},
        {0.09973379824061199, 0.9950141554211683},
        {0.04195860569820542, 0.9991193499316599},
        {0.79031744413336, 0.6126975905767162},
        {-0.7467622035600079, 0.6650911300973734},
        {-0.3992911184028081, -0.9168241940332044},
        {0.9308133079502352, 0.36549498730179686},
        {0.38127527057552224, 0.9244615557434297},
        {0.9337205894304965, 0.35800259897599357},
        {0.6347368798988079, 0.7727283437899288}};
    
    private static final int GRAD_MASK = grads.length - 1;

    // x and y in cartesian coords
    // i and j are in the coordinates of the simplex grid
    // 
    //  XY-coords
    //
    //  p3
    //    * - - - - * p2
    //     \       / \
    //      \     /   \
    //       \   /     \
    //        \ /       \
    //         * _       \
    //       p1     ~  -  *
    //                     p3
    //
    // x = i + c * (i + j)
    // y = j + c * (i + j)
    //   where c = (sqrt(3) - 1) / 2
    //
    //  IJ-coords
    //  
    //  p3         p2
    //   * - - - - *
    //   |       / |
    //   |     /   |
    //   |   /     |
    //   | /       |
    //   * - - - - *
    //  p1         p3
    //
    public static double Noise(double x, double y) {
        // Transformations constant for xy -> ij. 
        final double C = (Math.sqrt(3) - 1) / 2.0;

        // transform x,y to IJ coords
        double t_x = x + C * (x + y);
        double t_y = y + C * (x + y);

        // determine the lower left vertices transformed coords
        int t_p1_x = (t_x>0)?(int)t_x:(int)(x-1);
        int t_p1_y = (t_y>0)?(int)t_y:(int)(y-1);

        final double D = (Math.sqrt(3) - 3)/6;

        // transform back to the simplex grid
        double p1_x = t_p1_x + D * (t_p1_x + t_p1_y);
        double p1_y = t_p1_y + D * (t_p1_x + t_p1_y);
        
        // determine the distance between p1 and the input point
        double p1_dx = x - p1_x;
        double p1_dy = y - p1_y;

        // determine the difference to the upper right point
        double p2_dx = p1_dx - 1 - 2*D;
        double p2_dy = p1_dy - 1 - 2*D;

        // determine the last point IJ coords by finding what section of the transformed 2-simplex square the point lies in
        int t_p3_dx, t_p3_dy;
        if(t_x - t_p1_x < t_y - t_p1_y){
            // upper left simplex
            t_p3_dx = 0;
            t_p3_dy = 1;
        } else {
            // lower right simplex
            t_p3_dx = 1;
            t_p3_dy = 0;
        }

        double p3_dx = p1_dx - t_p3_dx - D;
        double p3_dy = p1_dy - t_p3_dy - D;

        // populate the gradients for each point
        double p1_gx = grads[perms[(t_p1_x + perms[t_p1_y&255])&255]&GRAD_MASK][0];
        double p1_gy = grads[perms[(t_p1_x + perms[t_p1_y&255])&255]&GRAD_MASK][1];
        double p2_gx = grads[perms[(t_p1_x + 1 + perms[(t_p1_y + 1)&255])&255]&GRAD_MASK][0];
        double p2_gy = grads[perms[(t_p1_x + 1 + perms[(t_p1_y + 1)&255])&255]&GRAD_MASK][1];
        double p3_gx = grads[perms[(t_p1_x + t_p3_dx + perms[(t_p1_y + t_p3_dy)&255])&255]&GRAD_MASK][0];
        double p3_gy = grads[perms[(t_p1_x + t_p3_dx + perms[(t_p1_y + t_p3_dy)&255])&255]&GRAD_MASK][1];

        // determine the effect of each point (z^2 value of sphere with radius sqrt(2)/2)
        double r2 = 0.5;
        double p1_z2 = r2 - p1_dx*p1_dx - p1_dy*p1_dy;
        double p2_z2 = r2 - p2_dx*p2_dx - p2_dy*p2_dy;
        double p3_z2 = r2 - p3_dx*p3_dx - p3_dy*p3_dy;

        // determine the contribution from each point
        double p1_d, p2_d, p3_d;
        if(p1_z2 < 0){
            p1_d = 0;
        } else {
            // There's a nice interpolation curve from z^8. We have z^2, so z2^4
            p1_z2 *= p1_z2;
            p1_z2 *= p1_z2;
            // multiply by the dot product of the surflet and the direction
            p1_d = p1_z2 * (p1_gx*p1_dx + p1_gy*p1_dy);
        }

        if(p2_z2 < 0){
            p2_d = 0;
        } else {
            p2_z2 *= p2_z2;
            p2_z2 *= p2_z2;
            p2_d = p2_z2 * (p2_gx*p2_dx + p2_gy*p2_dy);
        }

        if(p3_z2 < 0){
            p3_d = 0;
        } else {
            p3_z2 *= p3_z2;
            p3_z2 *= p3_z2;
            p3_d = p3_z2 * (p3_gx*p3_dx + p3_gy*p3_dy);
        }

        return 70.0 * (p1_d + p2_d + p3_d);
    }
}
