package com.fsegt.douanetemporelle;

import static org.junit.Assert.*;
import org.junit.Test;

import com.fsegt.douanetemporelle.utils.IntentKeys;
import com.fsegt.douanetemporelle.utils.ParadoxUtils;

/**
 * اختبارات الوحدة (Unit Tests) للتحقق من صحة منطق الجمارك الزمنية
 * 1. اختبار خوارزمية الأعداد الأولية (Nombres Premiers)
 * 2. اختبار كشف المفارقة الزمنية (Paradoxe Temporel)
 * 3. اختبار توليد مفاتيح Intent مسبوقة برقم الطالب
 */
public class ParadoxUtilsTest {

    @Test
    public void testNombresPremiers() {
        // 1. الأعداد الأولية الشهيرة
        assertTrue("2 يجب أن يكون عدداً أولياً", ParadoxUtils.estNombrePremier(2));
        assertTrue("3 يجب أن يكون عدداً أولياً", ParadoxUtils.estNombrePremier(3));
        assertTrue("5 يجب أن يكون عدداً أولياً", ParadoxUtils.estNombrePremier(5));
        assertTrue("7 يجب أن يكون عدداً أولياً", ParadoxUtils.estNombrePremier(7));
        assertTrue("11 يجب أن يكون عدداً أولياً", ParadoxUtils.estNombrePremier(11));
        assertTrue("13 يجب أن يكون عدداً أولياً", ParadoxUtils.estNombrePremier(13));
        assertTrue("17 يجب أن يكون عدداً أولياً", ParadoxUtils.estNombrePremier(17));
        assertTrue("19 يجب أن يكون عدداً أولياً", ParadoxUtils.estNombrePremier(19));
        assertTrue("23 يجب أن يكون عدداً أولياً", ParadoxUtils.estNombrePremier(23));

        // 2. الأعداد غير الأولية
        assertFalse("0 ليس عدداً أولياً", ParadoxUtils.estNombrePremier(0));
        assertFalse("1 ليس عدداً أولياً", ParadoxUtils.estNombrePremier(1));
        assertFalse("4 ليس أولياً", ParadoxUtils.estNombrePremier(4));
        assertFalse("6 ليس أولياً", ParadoxUtils.estNombrePremier(6));
        assertFalse("9 ليس أولياً", ParadoxUtils.estNombrePremier(9));
        assertFalse("15 ليس أولياً", ParadoxUtils.estNombrePremier(15));
    }

    @Test
    public void testDetectionParadoxe() {
        // حالة 1: دافع "Modification du passé" وفارق أولي (مثلاً: ولادة 1990، وجهة 1971 -> الفارق 19 أولي)
        // يجب أن يكشف المفارقة (true)
        boolean paradoxe1 = ParadoxUtils.verifierRisqueParadoxe("Modification du passé", 1971, 1990);
        assertTrue("يجب كشف المفارقة إذا كان الدافع تعديل الماضي والفارق أولياً", paradoxe1);

        // حالة 2: دافع "Tourisme" حتى وإن كان الفارق عدداً أولياً
        // لا توجد مفارقة لأن الدافع ليس تعديل الماضي
        boolean paradoxe2 = ParadoxUtils.verifierRisqueParadoxe("Tourisme", 1971, 1990);
        assertFalse("لا يجب كشف المفارقة إذا كان الدافع سياحة", paradoxe2);

        // حالة 3: دافع "Modification du passé" ولكن الفارق غير أولي (مثلاً: 1990 و 1970 -> الفارق 20 ليس أولياً)
        boolean paradoxe3 = ParadoxUtils.verifierRisqueParadoxe("Modification du passé", 1970, 1990);
        assertFalse("لا يجب كشف المفارقة إذا كان الفارق غير أولي", paradoxe3);
    }

    @Test
    public void testGenerationCleIntent() {
        // التحقق من أن المفتاح يتم توليده بصيغة "MATRICULE_NOM" بدون كتابة ثابتة
        String cle = IntentKeys.buildKey("1234", IntentKeys.KEY_NOM);
        assertEquals("1234_NOM_VOYAGEUR", cle);
    }
}
