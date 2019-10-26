import edu.princeton.cs.algs4.StdAudio;
import es.datastructur.synthesizer.GuitarString;
import huglife.StdDraw;


public class GuitarHero {
    private static final String keyboard = "q2we4r5ty7u8i9op-[=zxdcfvgbnjmk,.;/' ";

    public static void main(String[] args) {
        GuitarString[] guitarStrings = new GuitarString[37];
        for (int index = 0; index != 37; index++) {
            guitarStrings[index] = new GuitarString(440*Math.pow(2, (index-24)/12));
        }

        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                int index = keyboard.indexOf(key);
                if (index < 0) {
                    continue;
                }
                guitarStrings[index].pluck();
            }

            double sample = 0.0;
            for (int i=0; i!=37; i++) {
                sample += guitarStrings[i].sample();
            }

            StdAudio.play(sample);

            for (int i=0; i!=37; i++) {
                guitarStrings[i].tic();
            }

        }



    }

}
