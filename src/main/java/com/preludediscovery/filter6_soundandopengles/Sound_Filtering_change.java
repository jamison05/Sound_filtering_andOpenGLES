package com.preludediscovery.filter6_soundandopengles;

/**
 * Created by deepj on 12/30/2017.
 */

public class Sound_Filtering_change {

    /**
     * Constructor for a multi-channel LPRezFilter with frequency and resonance
     * specified by floats.
     *
     * @param con
     *            The audio context.
     * @param channels
     *            The number of channels.
     * @param freq
     *            The filter cut-off frequency.
     * @param res
     *            The resonance.
     */
//    public int LPRezFilter(AudioContext con, int channels, float freq, float res) {
//
//        setFrequency(freq).setRes(res);
//
//        return 0;
//    }
//

    protected float freq = 100;
    protected float res = .5f, _2pi_over_sr, cosw = 0;
    protected float a1, a2, b0;
    int bufferSize = 512;
    private float y1 = 0, y2 = 0;
    private float[] y1m, y2m;
    private int channels;


    protected void calcVals() {
        a1 = -2 * res * cosw;
        a2 = res * res;
        b0 = 1 + a1 + a2;
    }

    public float freqUGen; //If the value of the frequency is changing. for filtering.
    public float resUGen;  //If the value of the resonance is changing for filtering.
    public void calculateBuffer(float[] bufIn, float[] bufOut) {

        float[] bi = bufIn; //The orignal code is for 2 channels.  //I don't think I wiil have that much detail in the filtering
        //do to platform constraints.
        float[] bo = bufOut;
        if (channels == 1) {





//                freqUGen.update();
//                resUGen.update();

                cosw = (float) (Math.cos(_2pi_over_sr * (freq = freqUGen)));
                //This is to keep the res at a static level.
                //The below code is to set the value to correct ranges.
                if ((res = resUGen) > .999999f) {
                    res = .999999f;
                } else if (res < 0) {
                    res = 0;
                }
                calcVals();  //Function for iteratively calculting the filter valkues
                            //very important before feeding to the initial output.
                bo[0] = bi[0] * b0 - a1 * y1 - a2 * y2;

                cosw = (float) (Math.cos(_2pi_over_sr * (freq = freqUGen)));
                if ((res = resUGen) > .999999f) {
                    res = .999999f;
                } else if (res < 0) {
                    res = 0;
                }
                calcVals();
                bo[1] = bi[1] * b0 - a1 * bo[0] - a2 * y1;

                // Main loop for the rest of the buffer.
                for (int currsamp = 2; currsamp < bufferSize; currsamp++) {

                    cosw = (float) (Math.cos(_2pi_over_sr * (freq = freqUGen)));
                    if ((res = resUGen) > .999999f) {
                        res = .999999f;
                    } else if (res < 0) {
                        res = 0;
                    }
                    calcVals();
                    //I don't have the time to break down every single part of the equation and model it to the LpRez function
                    //Howewver what I do know is that it is taking the frequenzy and substracting sound intensity
                    //through through the cosine equation which is basically tracting on the frequenchy and periodicity of sound.
                    //It goes to each element recursively goinng through each parts of the sound.

                    //a1 a2 and b0 are all based on the given frequency and resonance.
                    //cosw that is based on samping rate (_2pi_over_sr) is substracted also part of this equation.

                    bo[currsamp] = bi[currsamp] * b0 - a1 * bo[currsamp - 1] - a2 * bo[currsamp - 2];
                }

            }

            y2 = bo[bufferSize - 2];
//            if (Float.isNaN(y1 = bo[bufferSize - 1])) {
//                reset();
//            }

        }


}
