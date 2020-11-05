package ud.example.latigo;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.widget.CheckBox;




public class MainActivity extends AppCompatActivity implements SensorEventListener{

    private SoundPool sPool;
    private int sound1;
    private MediaPlayer player;
    private AudioManager audioManager;
    private CheckBox checkBox;
    private float X,Y,Z;
    private SensorManager Sensores;
    private Sensor SensorAce;
    private long lastUpdate = 0;
    private static final int sacudida = 1200;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            sPool = new SoundPool.Builder()
                    .setAudioAttributes(attributes)
                    .setMaxStreams(10)
                    .build();
        } else {

            sPool = new SoundPool(6, AudioManager.STREAM_MUSIC, 0);

        }

        checkBox = findViewById(R.id.checkBox);
        sound1 = sPool.load(this, R.raw.latigosound, 1);
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        Z = 0;

        Sensores = (SensorManager) getSystemService(SENSOR_SERVICE);
        SensorAce = Sensores.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        Sensores.registerListener(this, SensorAce, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor MySensor = sensorEvent.sensor;
        if(MySensor.getType()==Sensor.TYPE_ACCELEROMETER){
            try{

                float Za = sensorEvent.values[2];

               long curTime = System.currentTimeMillis();
                if ((curTime - lastUpdate) > 100) {
                    long diffTime = (curTime - lastUpdate);
                    lastUpdate = curTime;

                    float velocidad = Math.abs( Za - Z)/ diffTime * 10000;

                    if (velocidad > sacudida && checkBox.isChecked()==true) {
                        sPool.play(sound1,1,1,1,0,1);
                    }


                    Z=sensorEvent.values[2];
                }




            }catch (Exception Ex){

            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}