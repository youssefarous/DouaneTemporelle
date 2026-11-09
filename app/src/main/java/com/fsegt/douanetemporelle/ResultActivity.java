package com.fsegt.douanetemporelle;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.fsegt.douanetemporelle.utils.IntentKeys;

/**
 * النشاط الثالث : الحكم النهائي وقرار العبور (ResultActivity)
 * 
 * الخصائص والوظائف:
 * 1. شاشة بسيطة تستقبل قرار الحكم واسم المسافر.
 * 2. تغيير لون الخلفية ديناميكياً بحسب القرار (أخضر للموافقة / أحمر للرفض والاعتقال).
 * 3. عرض رسالة مخصصة باسم المسافر:
 *    - عند الموافقة: "Bon voyage [Nom] !"
 *    - عند الرفض: "[Nom] est en état d'arrestation temporelle !"
 * 4. زر "Nouveau voyageur" للعودة إلى النشاط الأول (MainActivity) مع إغلاق كافة الأنشطة السابقة.
 */
public class ResultActivity extends AppCompatActivity {

    // 1. تعريف عناصر الواجهة (معرفات تنتهي بالحروف الأولى _nj)
    private LinearLayout llRacineVerdictNj;
    private ImageView ivIconeVerdictNj;
    private TextView tvTitreVerdictNj;
    private TextView tvMessagePersonnaliseNj;
    private Button btnNouveauVoyageurNj;

    // 2. رقم بطاقة الطالب لاسترجاع المفاتيح
    private static final String MATRICULE_ETUDIANT = IntentKeys.MATRICULE_ETUDIANT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        // 3. ربط العناصر البرمجية
        initialiserVues();

        // 4. قراءة البيانات المرسلة وتحديث واجهة المستخدم (اللون والنصوص)
        traiterVerdictEtMettreAJourUI();

        // 5. برمجة زر العودة لتسجيل مسافر جديد
        configurerBoutonNouveauVoyageur();
    }

    /**
     * دالة لربط عناصر الواجهة بمعرفاتها
     */
    private void initialiserVues() {
        llRacineVerdictNj = findViewById(R.id.ll_racine_verdict_nj);
        ivIconeVerdictNj = findViewById(R.id.iv_icone_verdict_nj);
        tvTitreVerdictNj = findViewById(R.id.tv_titre_verdict_nj);
        tvMessagePersonnaliseNj = findViewById(R.id.tv_message_personnalise_nj);
        btnNouveauVoyageurNj = findViewById(R.id.btn_nouveau_voyageur_nj);
    }

    /**
     * دالة لاسترجاع النتيجة وتغيير لون الخلفية والرسائل بناءً على القرار
     */
    private void traiterVerdictEtMettreAJourUI() {
        Intent intent = getIntent();
        boolean verdictApprouve = false;
        String nomVoyageur = "Voyageur";

        if (intent != null) {
            // 1. توليد المفاتيح عبر الدالة الثابتة
            String cleVerdict = IntentKeys.buildKey(MATRICULE_ETUDIANT, IntentKeys.KEY_VERDICT);
            String cleNom = IntentKeys.buildKey(MATRICULE_ETUDIANT, IntentKeys.KEY_NOM);

            verdictApprouve = intent.getBooleanExtra(cleVerdict, false);
            String nomRecu = intent.getStringExtra(cleNom);
            if (nomRecu != null && !nomRecu.trim().isEmpty()) {
                nomVoyageur = nomRecu.trim();
            }
        }

        // 2. تعديل الواجهة بحسب قرار الجمارك الزمنية
        if (verdictApprouve) {
            // أ) حالة الموافقة (Approuvé):
            // - خلفية خضراء تعبر عن الأمان
            llRacineVerdictNj.setBackgroundColor(ContextCompat.getColor(this, R.color.verdict_approved_bg));
            // - أيقونة ختم الموافقة
            ivIconeVerdictNj.setImageResource(R.drawable.ic_approved);
            // - عنوان الموافقة
            tvTitreVerdictNj.setText(R.string.verdict_status_approved);
            // - رسالة شخصية: "Bon voyage [Nom] !"
            String message = getString(R.string.msg_bon_voyage, nomVoyageur);
            tvMessagePersonnaliseNj.setText(message);

        } else {
            // ب) حالة الرفض والاعتقال (Refusé / Risque de paradoxe):
            // - خلفية حمراء تعبر عن الخطر وحالة الطوارئ
            llRacineVerdictNj.setBackgroundColor(ContextCompat.getColor(this, R.color.verdict_refused_bg));
            // - أيقونة التحذير وحالة الطوارئ
            ivIconeVerdictNj.setImageResource(R.drawable.ic_arrested);
            // - عنوان الرفض والاعتقال
            tvTitreVerdictNj.setText(R.string.verdict_status_refused);
            // - رسالة شخصية: "[Nom] est en état d'arrestation temporelle !"
            String message = getString(R.string.msg_arrestation, nomVoyageur);
            tvMessagePersonnaliseNj.setText(message);
        }
    }

    /**
     * دالة لإعداد زر "مسافر جديد" (Nouveau voyageur):
     * تقوم بالعودة إلى MainActivity مع إغلاق الأنشطة السابقة من مكدس المهام (Task Stack)
     */
    private void configurerBoutonNouveauVoyageur() {
        btnNouveauVoyageurNj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 1. إنشاء Intent للعودة إلى النشاط الأول MainActivity
                Intent intentRetour = new Intent(ResultActivity.this, MainActivity.class);

                // 2. استخدام أعلام مسح المكدس لإغلاق جميع الأنشطة السابقة وضمان انطلاقة جديدة نظيفة
                intentRetour.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

                // 3. بدء النشاط الأول وإغلاق النشاط الحالي
                startActivity(intentRetour);
                finish();
            }
        });
    }
}
