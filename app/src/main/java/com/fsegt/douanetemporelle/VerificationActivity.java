package com.fsegt.douanetemporelle;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.fsegt.douanetemporelle.utils.IntentKeys;
import com.fsegt.douanetemporelle.utils.ParadoxUtils;

/**
 * النشاط الثاني : ماسح المفارقات والتذكرة الزمنية (VerificationActivity)
 * 
 * الخصائص والوظائف:
 * 1. استرجاع البيانات المرسلة من النشاط الأول باستخدام المفاتيح الديناميكية.
 * 2. حساب وعرض "العمر الزمني" (الفارق بين سنة الوجهة وسنة الولادة).
 * 3. فحص شرط المفارقة الزمنية:
 *    إذا كان الدافع "Modification du passé" والفارق بين سنة الوجهة وسنة الولادة عدداً أولياً،
 *    يتم تعطيل (disable) زر "الموافقة" فور تحميل النشاط.
 * 4. زران (موافقة / رفض) ينقلان المسافر إلى النشاط الثالث (ResultActivity)
 *    مع تمرير القرار النهائي (booléen) واسم المسافر.
 */
public class VerificationActivity extends AppCompatActivity {

    // 1. تعريف عناصر واجهة المستخدم (تنتهي جميعها بـ _nj)
    private TextView tvResumeNomNj;
    private TextView tvResumeAgeTemporelNj;
    private TextView tvResumeMotifNj;
    private TextView tvAlerteParadoxeNj;
    private Button btnApprouverSautNj;
    private Button btnRefuserSautNj;

    // 2. المتغيرات المحلية لتخزين البيانات المسترجعة
    private String nomVoyageur;
    private int anneeNaissance;
    private int anneeDestination;
    private String motifVoyage;
    private int ageTemporelCalcule;

    // 3. رقم بطاقة الطالب المستخدم لتوليد المفاتيح
    private static final String MATRICULE_ETUDIANT = IntentKeys.MATRICULE_ETUDIANT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        // 4. ربط العناصر البرمجية بالواجهة
        initialiserVues();

        // 5. استرجاع البيانات المرسلة من MainActivity
        recupererDonneesIntent();

        // 6. حساب العمر الزمني وعرض الملخص داخل بطاقة الجواز
        calculerEtAfficherResume();

        // 7. التحقق من شرط المفارقة وتطبيق القيد البرمجي الإجباري
        verifierConditionParadoxe();

        // 8. إعداد أحداث النقر على أزرار القرار النهائي
        configurerBoutonsVerdict();
    }

    /**
     * دالة لربط عناصر التصميم بمعرفاتها التي تنتهي بـ _nj
     */
    private void initialiserVues() {
        tvResumeNomNj = findViewById(R.id.tv_resume_nom_nj);
        tvResumeAgeTemporelNj = findViewById(R.id.tv_resume_age_temporel_nj);
        tvResumeMotifNj = findViewById(R.id.tv_resume_motif_nj);
        tvAlerteParadoxeNj = findViewById(R.id.tv_alerte_paradoxe_nj);
        btnApprouverSautNj = findViewById(R.id.btn_approuver_saut_nj);
        btnRefuserSautNj = findViewById(R.id.btn_refuser_saut_nj);
    }

    /**
     * دالة لاسترجاع البيانات من Intent عبر المفاتيح المبنية بالدالة الثابتة
     */
    private void recupererDonneesIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            String cleNom = IntentKeys.buildKey(MATRICULE_ETUDIANT, IntentKeys.KEY_NOM);
            String cleNaissance = IntentKeys.buildKey(MATRICULE_ETUDIANT, IntentKeys.KEY_ANNEE_NAISSANCE);
            String cleDestination = IntentKeys.buildKey(MATRICULE_ETUDIANT, IntentKeys.KEY_ANNEE_DESTINATION);
            String cleMotif = IntentKeys.buildKey(MATRICULE_ETUDIANT, IntentKeys.KEY_MOTIF);

            nomVoyageur = intent.getStringExtra(cleNom);
            anneeNaissance = intent.getIntExtra(cleNaissance, 2000);
            anneeDestination = intent.getIntExtra(cleDestination, 2000);
            motifVoyage = intent.getStringExtra(cleMotif);
        }

        // قيم افتراضية للأمان في حال كانت السلسلة فارغة
        if (nomVoyageur == null) {
            nomVoyageur = "Voyageur Inconnu";
        }
        if (motifVoyage == null) {
            motifVoyage = "Tourisme";
        }
    }

    /**
     * دالة لحساب العمر الزمني وعرض التقرير داخل بطاقة التذكرة
     */
    private void calculerEtAfficherResume() {
        // 1. حساب العمر الزمني: سنة الوجهة ناقص سنة الولادة
        ageTemporelCalcule = anneeDestination - anneeNaissance;

        // 2. تنسيق وعرض اسم المسافر
        tvResumeNomNj.setText(String.format("Voyageur : %s", nomVoyageur));

        // 3. صياغة وتوضيح العمر الزمني المحسوب للمسافر
        String directionTemps = ageTemporelCalcule >= 0 ? "dans le futur" : "dans le passé";
        tvResumeAgeTemporelNj.setText(String.format("Âge temporel : %d ans (%s)", 
                ageTemporelCalcule, directionTemps));

        // 4. عرض دافع السفر المصرح به
        tvResumeMotifNj.setText(String.format("Motif : %s", motifVoyage));
    }

    /**
     * دالة لتطبيق القيد الصارم للمشروع:
     * إذا كان الدافع 'Modification du passé' وكان الفارق عدداً أولياً،
     * يتم تعطيل زر الموافقة فور تحميل الواجهة.
     */
    private void verifierConditionParadoxe() {
        // 1. استدعاء دالة التحقق من المفارقة الزمنية
        boolean risqueParadoxe = ParadoxUtils.verifierRisqueParadoxe(motifVoyage, anneeDestination, anneeNaissance);

        if (risqueParadoxe) {
            // 2. تعطيل زر الموافقة مباشرة عند تحميل الواجهة (Disabled)
            btnApprouverSautNj.setEnabled(false);
            btnApprouverSautNj.setAlpha(0.4f); // تخفيض الشفافية لإبراز حالة التعطيل بصرياً

            // 3. إظهار نص التنبيه التحذيري باللون الأحمر
            int differenceAbsolue = Math.abs(anneeDestination - anneeNaissance);
            tvAlerteParadoxeNj.setVisibility(View.VISIBLE);
            tvAlerteParadoxeNj.setText(String.format(
                    "⚠️ ALERTE PARADOXE TEMPOREL !\nMotif : Modification du passé.\nÉcart temporel : %d (Nombre Premier).\nLe bouton 'Approuver' est strictement verrouillé !",
                    differenceAbsolue));
        } else {
            // في حال عدم وجود خطر، زر الموافقة يظل متاحاً
            btnApprouverSautNj.setEnabled(true);
            btnApprouverSautNj.setAlpha(1.0f);
            tvAlerteParadoxeNj.setVisibility(View.GONE);
        }
    }

    /**
     * دالة لإعداد مستمعي الأزرار لنقل القرار النهائي إلى النشاط الثالث (ResultActivity)
     */
    private void configurerBoutonsVerdict() {
        // 1. زر الموافقة على السفر عبر الزمن (Verdict = true)
        btnApprouverSautNj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lancerActiviteVerdict(true);
            }
        });

        // 2. زر الرفض بسبب خطر المفارقة (Verdict = false)
        btnRefuserSautNj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lancerActiviteVerdict(false);
            }
        });
    }

    /**
     * دالة مساعدة لإنشاء Intent ونقل النتيجة واسم المسافر إلى النشاط الثالث
     * 
     * @param verdictFinal قرار الجمارك (true = موافقة، false = رفض)
     */
    private void lancerActiviteVerdict(boolean verdictFinal) {
        // 1. إنشاء Intent صريح نحو ResultActivity
        Intent intentResultat = new Intent(VerificationActivity.this, ResultActivity.class);

        // 2. بناء المفاتيح باستخدام الدالة الثابتة
        String cleVerdict = IntentKeys.buildKey(MATRICULE_ETUDIANT, IntentKeys.KEY_VERDICT);
        String cleNom = IntentKeys.buildKey(MATRICULE_ETUDIANT, IntentKeys.KEY_NOM);

        // 3. وضع المعطيات المطلوبة (القرار النهائي البوليني + اسم المسافر)
        intentResultat.putExtra(cleVerdict, verdictFinal);
        intentResultat.putExtra(cleNom, nomVoyageur);

        // 4. تشغيل النشاط الثالث
        startActivity(intentResultat);
    }
}
