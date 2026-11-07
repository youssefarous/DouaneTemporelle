package com.fsegt.douanetemporelle;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.fsegt.douanetemporelle.utils.IntentKeys;

/**
 * النشاط الأول : استمارة تسجيل المسافر الزمني (MainActivity)
 * 
 * الهدف:
 * 1. جمع بيانات المسافر: الاسم، سنة الولادة، سنة الوجهة، ودافع السفر.
 * 2. التحقق من صحة المدخلات واكتمالها.
 * 3. الانتقال إلى النشاط الثاني (VerificationActivity) عبر Intent صريح (Explicit Intent).
 * 4. تمرير البيانات باستخدام مفاتيح ديناميكية مبنية عبر دالة ثابتة مسبوقة بآخر 4 أرقام من بطاقة الطالب.
 */
public class MainActivity extends AppCompatActivity {

    // 1. تعريف عناصر واجهة المستخدم (معرفات تنتهي بـ _nj)
    private EditText etNomVoyageurNj;
    private EditText etAnneeNaissanceNj;
    private EditText etAnneeDestinationNj;
    private Spinner spMotifVoyageNj;
    private Button btnInspecterProfilNj;

    // 2. رقم بطاقة الطالب (آخر 4 أرقام - يمكن استبدالها برقم أي طالب في المجموعة)
    private static final String MATRICULE_ETUDIANT = IntentKeys.MATRICULE_ETUDIANT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 3. ربط عناصر الواجهة بمتغيرات الجافا بواسطة معرفاتها
        initialiserVues();

        // 4. إعداد مستمع النقر لزر "فحص الملف الشخصي" (Inspecter le profil)
        btnInspecterProfilNj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // تنفيذ عملية التحقق والإرسال
                traiterEtEnvoyerDonnees();
            }
        });
    }

    /**
     * دالة لربط كائنات الجافا بالعناصر المعرفة في ملف XML
     */
    private void initialiserVues() {
        etNomVoyageurNj = findViewById(R.id.et_nom_voyageur_nj);
        etAnneeNaissanceNj = findViewById(R.id.et_annee_naissance_nj);
        etAnneeDestinationNj = findViewById(R.id.et_annee_destination_nj);
        spMotifVoyageNj = findViewById(R.id.sp_motif_voyage_nj);
        btnInspecterProfilNj = findViewById(R.id.btn_inspecter_profil_nj);
    }

    /**
     * دالة للتحقق من صحة المدخلات وتجهيز Intent صريح للانتقال إلى النشاط 2
     */
    private void traiterEtEnvoyerDonnees() {
        // 1. استخراج النصوص المدخلة من قبل المستخدم
        String nom = etNomVoyageurNj.getText().toString().trim();
        String strNaissance = etAnneeNaissanceNj.getText().toString().trim();
        String strDestination = etAnneeDestinationNj.getText().toString().trim();
        String motif = spMotifVoyageNj.getSelectedItem() != null ? 
                       spMotifVoyageNj.getSelectedItem().toString() : "Tourisme";

        // 2. التحقق من عدم ترك أي حقل فارغاً
        if (nom.isEmpty() || strNaissance.isEmpty() || strDestination.isEmpty()) {
            Toast.makeText(MainActivity.this, 
                    getString(R.string.error_champs_vides), 
                    Toast.LENGTH_SHORT).show();
            return;
        }

        // 3. تحويل السنوات إلى أرقام صحيحة والتحقق من صلاحيتها المنطقية
        int anneeNaissance;
        int anneeDestination;
        try {
            anneeNaissance = Integer.parseInt(strNaissance);
            anneeDestination = Integer.parseInt(strDestination);
        } catch (NumberFormatException e) {
            Toast.makeText(MainActivity.this, 
                    getString(R.string.error_annee_invalide), 
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (anneeNaissance <= 0 || anneeNaissance > 9999 || anneeDestination <= 0 || anneeDestination > 9999) {
            Toast.makeText(MainActivity.this, 
                    getString(R.string.error_annee_invalide), 
                    Toast.LENGTH_SHORT).show();
            return;
        }

        // 4. إنشاء نية صريحة (Explicit Intent) للتوجه إلى النشاط الثاني VerificationActivity
        Intent intentVerification = new Intent(MainActivity.this, VerificationActivity.class);

        // 5. إضافة البيانات الإضافية (Extras) باستخدام المفاتيح المولدة ديناميكياً
        // تطبيق شرط المشروع: المفاتيح تبنى بواسطة دالة ثابتة وتسبق برقم الطالب
        String cleNom = IntentKeys.buildKey(MATRICULE_ETUDIANT, IntentKeys.KEY_NOM);
        String cleNaissance = IntentKeys.buildKey(MATRICULE_ETUDIANT, IntentKeys.KEY_ANNEE_NAISSANCE);
        String cleDestination = IntentKeys.buildKey(MATRICULE_ETUDIANT, IntentKeys.KEY_ANNEE_DESTINATION);
        String cleMotif = IntentKeys.buildKey(MATRICULE_ETUDIANT, IntentKeys.KEY_MOTIF);

        intentVerification.putExtra(cleNom, nom);
        intentVerification.putExtra(cleNaissance, anneeNaissance);
        intentVerification.putExtra(cleDestination, anneeDestination);
        intentVerification.putExtra(cleMotif, motif);

        // 6. تشغيل النشاط الثاني
        startActivity(intentVerification);
    }
}
