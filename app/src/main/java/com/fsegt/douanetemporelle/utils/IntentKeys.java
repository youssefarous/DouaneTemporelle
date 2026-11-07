package com.fsegt.douanetemporelle.utils;

/**
 * فئة مساعدة لإدارة وتوليد مفاتيح Intent (Intent Keys Helper)
 * 
 * الشرط الصارم في نص المشروع:
 * "Toutes les clés utilisées dans les putExtra doivent être préfixées par les 4 derniers chiffres
 *  de la carte d’étudiant d’un des membres (ex: '1234_NOM_VOYAGEUR').
 *  Ces clés ne doivent pas être codées en dur, mais construites via une méthode statique
 *  qui prend le matricule en paramètre."
 *
 * هذه الفئة تضمن عدم كتابة المفاتيح بشكل ثابت (hardcoded) بل يتم توليدها ديناميكياً
 * بواسطة دالة ثابتة تستقبل آخر 4 أرقام من بطاقة الطالب.
 */
public class IntentKeys {

    // 1. آخر 4 أرقام من بطاقة أحد أعضاء الفريق (يمكن تغييرها هنا لتطابق رقم بطاقة الطالب الفعلية)
    public static final String MATRICULE_ETUDIANT = "1234";

    // 2. الأسماء الأساسية للمتغيرات المنقولة بين الأنشطة (بدون بادئة)
    public static final String KEY_NOM = "NOM_VOYAGEUR";
    public static final String KEY_ANNEE_NAISSANCE = "ANNEE_NAISSANCE";
    public static final String KEY_ANNEE_DESTINATION = "ANNEE_DESTINATION";
    public static final String KEY_MOTIF = "MOTIF_VOYAGE";
    public static final String KEY_VERDICT = "VERDICT_FINAL";

    /**
     * دالة ثابتة (Static Method) تقوم ببناء المفتاح الكامل بدمج رقم بطاقة الطالب مع اسم المفتاح
     * 
     * مثال للتنفيذ:
     * buildKey("1234", "NOM_VOYAGEUR") -> النتيجة: "1234_NOM_VOYAGEUR"
     * 
     * @param matricule آخر 4 أرقام من بطاقة الطالب (ex: "1234")
     * @param nomCle    اسم المفتاح الأساسي (ex: "NOM_VOYAGEUR")
     * @return السلسلة النصية للمفتاح مسبوقة برقم البطاقة
     */
    public static String buildKey(String matricule, String nomCle) {
        // التأكد من عدم تمرير قيمة فارغة
        String prefixe = (matricule != null && !matricule.trim().isEmpty()) ? matricule.trim() : MATRICULE_ETUDIANT;
        return prefixe + "_" + nomCle;
    }

    /**
     * دالة مساعدة إضافية تبني المفتاح باستخدام الرقم الافتراضي المسجل في الفئة
     * 
     * @param nomCle اسم المفتاح الأساسي
     * @return المفتاح مع البادئة الافتراضية
     */
    public static String getKey(String nomCle) {
        return buildKey(MATRICULE_ETUDIANT, nomCle);
    }
}
