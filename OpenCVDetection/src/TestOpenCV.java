import java.util.ArrayList;
import java.util.List;


import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class TestOpenCV {

	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		// TODO Auto-generated method stub
		Mat img = Imgcodecs.imread("C:\\Users\\Mohammed\\Google Drive\\Second Year\\QHacks\\QHacks2017\\OpenCVDetection\\src\\check5.png", Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);
//		Imgproc.cvtColor(img, img, Imgproc.COLOR_BGR2GRAY);
		Imgproc.blur(img, img, new Size(3, 3));
		Imgproc.Canny(img, img, 50,  95, 3, false);
		
		
		byte data[] = {0,1,0,1,1,1,0,1,0};
		//allocate Mat before calling put
		Mat kernel = new Mat(3,3, CvType.CV_8U);
		kernel.put( 0, 0, data );
		Imgproc.dilate(img, img, kernel);
		
		int max = -1;
		Point maxPt=null;
		for (int y = 0; y < img.size().height; y++) {
			for (int x = 0; x < img.size().width; x++) {
				if (img.get(y, x)[0] >= 128) {
					int area = Imgproc.floodFill(img, Mat.zeros(img.height()+2, img.width()+2, CvType.CV_8U), new Point(x,y), new Scalar(64,64,64));
					if (area > max) {
						maxPt = new Point(x, y);
						max = area;
					}
				}
			}
		}

		Imgproc.floodFill(img, Mat.zeros(img.height()+2, img.width()+2, CvType.CV_8U), maxPt, new Scalar(255));
		
		for (int y = 0; y < img.size().height; y++) {
			for (int x = 0; x < img.size().width; x++) {
				if (img.get(y, x)[0]==64 && x!=maxPt.x && y!=maxPt.y) {
					int area = Imgproc.floodFill(img, Mat.zeros(img.height()+2, img.width()+2, CvType.CV_8U), new Point(x,y), new Scalar(0));
					
				}
			}
		}
		
		
		List<MatOfPoint> contours = new ArrayList<>();
		Imgproc.findContours(img, contours,Mat.zeros(img.height(), img.width(), CvType.CV_8U), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
		
		MatOfPoint2f approx = new MatOfPoint2f();
		List<MatOfPoint2f> contours2f = new ArrayList<>();
		
		for(MatOfPoint a:contours){
			contours2f.add(new MatOfPoint2f(a.toArray()));
		}
		
		List<MatOfPoint> squares = new ArrayList<>();
		
        // test each contour
        for( int i = 0; i < contours.size(); i++ )
        {
            // approximate contour with accuracy proportional
            // to the contour perimeter
            Imgproc.approxPolyDP(contours2f.get(i),  approx, Imgproc.arcLength(contours2f.get(i), true)*0.02, true);

            // square contours should have 4 vertices after approximation
            // relatively large area (to filter out noisy contours)
            // and be convex.
            // Note: absolute value of an area is used because
            // area may be positive or negative - in accordance with the
            // contour orientation
            if( approx.height() == 4 &&
                Math.abs(Imgproc.contourArea(approx)) > 1000 &&
                Imgproc.isContourConvex(new MatOfPoint(approx.toArray())) )
            {
                double maxCosine = 0;

                for( int j = 2; j < 5; j++ )
                {
                    // find the maximum cosine of the angle between joint edges
                    double cosine = Math.abs(angle(approx.get(j%4,0), approx.get(j-2,0), approx.get(j-1,0)));
                    maxCosine = Math.max(maxCosine, cosine);
                }

                // if cosines of all angles are small
                // (all angles are ~90 degree) then write quandrange
                // vertices to resultant sequence
                if( maxCosine < 0.3 )
                    squares.add(new MatOfPoint(approx.toArray()));
            }
        }
		
		for (MatOfPoint matOfPoint : squares) {
			Imgproc.circle(img, new Point(matOfPoint.get(1, 0)), 20,new Scalar(255));
		}
		System.out.println(squares.size());
		Imgproc.erode(img, img, kernel);
		Imgcodecs.imwrite("C:\\Users\\Mohammed\\Google Drive\\Second Year\\QHacks\\QHacks2017\\OpenCVDetection\\src\\output.jpg", img);
	}
	
	static double angle( double[] ds, double[] ds2, double[] ds3 )
	{
	    double dx1 = ds[0] - ds3[0];
	    double dy1 = ds[1] - ds3[1];
	    double dx2 = ds2[0] - ds3[0];
	    double dy2 = ds2[1] - ds3[1];
	    return (dx1*dx2 + dy1*dy2)/Math.sqrt((dx1*dx1 + dy1*dy1)*(dx2*dx2 + dy2*dy2) + 1e-10);
	}

}
