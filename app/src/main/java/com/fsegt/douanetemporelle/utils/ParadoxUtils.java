package com.fsegt.douanetemporelle.utils;

/**
 * فئة مساعدة لفحص المفارقات الزمنية وحساب الأعداد الأولية (Paradox Utilities)
 * 
 * الشرط في نص المشروع:
 * "Si le motif du voyage est 'Modification du passé' ET que la différence entre
 *  l'année de destination et l'année de naissance est un nombre premier,
 *  le bouton 'Approuver' de l'Activité 2 doit être désactivé (disabled) dès le chargement de l'activité"
 */
public class ParadoxUtils {

    /**
     * دالة رياضية للتحقق مما إذا كان عدد صحيح عدداً أولياً (Nombre Premier)
     * العدد الأولي هو عدد طبيعي أكبر قطعاً من 1 لا يقبل القسمة إلا على 1 وعلى نفسه.
     * 
     * @param n العدد المراد اختباره
     * @return true إذا كان أولياً، false إذا لم يكن أولياً
     */
    public static boolean estNombrePremier(int n) {
        // 1. الأعداد الأقل من أو تساوي 1 ليست أولية
        if (n <= 1) {
            return false;
        }

        // 2. العددان 2 و 3 هما عددان أوليان
        if (n == 2 || n == 3) {
            return true;
        }

        // 3. استبعاد الأعداد الزوجية ومضاعفات 3
        if (n % 2 == 0 || n % 3 == 0) {
            return false;
        }

        // 4. خوارزمية سريعة لفحص القواسم المحتملة حتى جذر العدد (i * i <= n)
        for (int i = 5; i * i <= n; i += 6) {
            if (n % i == 0 || n % (i + 2) == 0) {
                return false;
            }
        }

        return true;
    }

    /**
     * دالة للتحقق من وجود خطر المفارقة الزمنية وفق شروط المشروع
     * 
     * @param motif            دافع السفر (Tourisme, Modification du passé, Fuite)
     * @param anneeDestination سنة الوصول المستهدفة
     * @param anneeNaissance   سنة ولادة المسافر
     * @return true في حال وجود خطر مفارقة يوجب تعطيل زر الموافقة
     */
    public static boolean verifierRisqueParadoxe(String motif, int anneeDestination, int anneeNaissance) {
        // 1. التحقق من أن الدافع هو "Modification du passé"
        boolean estModifPasse = "Modification du passé".equalsIgnoreCase(motif != null ? motif.trim() : "");

        // 2. حساب الفارق بين سنة الوجهة وسنة الولادة (بالقيمة المطلقة لضمان فحص الأعداد الأولية الطبيعية)
        int difference = Math.abs(anneeDestination - anneeNaissance);

        // 3. التحقق مما إذا كان الفارق عدداً أولياً
        boolean estPremier = estNombrePremier(difference);

        // 4. إذا تحقق الشرطان معاً، يوجد خطر مفارقة زمنية
        return estModifPasse && estPremier;
    }
}
