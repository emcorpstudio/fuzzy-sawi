package emcorp.studio.fuzzysawi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText edtTinggi, edtDiameter;
    Button btnProses;
    TextView tvScore, tvJenis;

    //Setup
    private double tinggiMax = 13.06;
    private double tinggiMin = 5.86;
    private double diameterMax = 0.42;
    private double diameterMin = 0.24;

    private double diameterBaik;
    private double diameterBuruk;
    private double tinggiBaik;
    private double tinggiBuruk;


    private double a_predikat1, a_predikat2, a_predikat3, a_predikat4;
    private double z1, z2, z3, z4;
    private double zTotal,z,a_pred_z ;
    private String jenis;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtTinggi = (EditText)findViewById(R.id.edtTinggi);
        edtDiameter = (EditText)findViewById(R.id.edtDiameter);
        btnProses = (Button)findViewById(R.id.btnProses);
        tvJenis = (TextView) findViewById(R.id.tvJenis);
        tvScore = (TextView) findViewById(R.id.tvScore);

        btnProses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!edtDiameter.getText().toString().equals("")&&!edtTinggi.getText().toString().equals("")){
                    fuzzifikasiTinggi(Double.valueOf(edtTinggi.getText().toString()));
                    fuzzifikasiDiameter(Double.valueOf(edtDiameter.getText().toString()));
                    mesinInferensiTsukamoto();
                    defuzzifikasi();
                }else{
                    Toast.makeText(getApplicationContext(),"Tinggi dan diameter harus diisi!",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void fuzzifikasiTinggi(double tinggi) {
        tinggiBaik = (tinggi-tinggiMin)/(tinggiMax-tinggiMin);
        tinggiBuruk = (tinggiMax-tinggi)/(tinggiMax-tinggiMin);
        System.out.println("Derajat keanggotaan tinggi baik : " + tinggiBaik);
        System.out.println("Derajat keanggotaan tinggi rendah : " + tinggiBuruk);
    }

    public void fuzzifikasiDiameter(double diameter) {
        diameterBaik = (diameter-diameterMin)/(diameterMax-diameterMin);
        diameterBuruk = (diameterMax-diameter)/(diameterMax-diameterMin);
        System.out.println("Derajat keanggotaan diameter baik : " + diameterBaik);
        System.out.println("Derajat keanggotaan diameter rendah : " + diameterBuruk);
    }

    public void mesinInferensiTsukamoto() {
        double max = 0;
        // IF TINGGI BAIK dan DIAMETER BAIK then Hasil Berkualitas
        a_predikat1 = Math.min(tinggiBaik, diameterBaik);
        z1 = (a_predikat1 * 14.55) + 0.47;
        System.out.println("a predikat 1 : " + a_predikat1 + " | " + "z1 : " + z1);
        max = z1;
        jenis = "Berkualitas";

        // IF TINGGI BURUK dan DIAMETER BAIK then Hasil Berkualitas
        a_predikat2 = Math.min(tinggiBuruk, diameterBaik);
        z2 = 15.02 - (a_predikat2 * 14.55);
        System.out.println("a predikat 2 : " + a_predikat2 + " | " + "z2 : " + z2);
        if(z2>=max){
            max = z2;
            jenis = "Berkualitas";
        }

        // IF TINGGI BURUK dan DIAMETER BURUK then Hasil Tidak Berkualitas
        a_predikat3 = Math.min(tinggiBuruk, diameterBuruk);
        z3 = 15.02 - (a_predikat3 * 14.55);
        System.out.println("a predikat 3 : " + a_predikat3+ " | " + "z3 : " + z3);
        if(z3>=max){
            max = z3;
            jenis = "Tidak Berkualitas";
        }

        // IF TINGGI BAIK dan DIAMETER BURUK then Hasil Tidak Berkualitas
        a_predikat4 = Math.min(tinggiBaik, diameterBuruk);
        z4 = (a_predikat4 * 14.55) + 0.47;
        System.out.println("a predikat 4 : " + a_predikat4 + " | " + "z4 : " + z4);
        if(z4>=max){
            max = z4;
            jenis = "Tidak Berkualitas";
        }
    }

    public void defuzzifikasi(){
        a_pred_z = (a_predikat1*z1)+(a_predikat2*z2)+(a_predikat3*z3)+(a_predikat4*z4) ;
        z = a_predikat1+a_predikat2+a_predikat3+a_predikat4 ;
        zTotal = a_pred_z/z ;
        tvJenis.setText(jenis);
        tvScore.setText(String.format("NIlai : %.2f", zTotal));
        System.out.println("Hasil : "+zTotal+" - "+jenis);
    }
}
