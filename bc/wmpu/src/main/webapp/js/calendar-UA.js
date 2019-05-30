// ** I18N

// Calendar UA language (Переклад на українську мову здійснив Олександр Ганджа)
// Author: Mihai Bazon, <mihai_bazon@yahoo.com>
// Encoding: any
// Distributed under the same terms as the calendar itself.

// For translators: please use UTF-8 if possible.  We strongly believe that
// Unicode is the answer to a real internationalized world.  Also please
// include your contact information in the header, as can be seen above.

// full day names
Calendar._DN = new Array
("Неділя",
 "Понеділок",
 "Вівторок",
 "Середа",
 "Четвер",
 "П'ятниця",
 "Субота",
 "Неділя");

// Please note that the following array of short day names (and the same goes
// for short month names, _SMN) isn't absolutely necessary.  We give it here
// for exemplification on how one can customize the short day names, but if
// they are simply the first N letters of the full name you can simply say:
//
//   Calendar._SDN_len = N; // short day name length
//   Calendar._SMN_len = N; // short month name length
//
// If N = 3 then this is not needed either since we assume a value of 3 if not
// present, to be compatible with translation files that were written before
// this feature.

// short day names
Calendar._SDN = new Array
("Нд",
 "Пн",
 "Вт",
 "Ср",
 "Чт",
 "Пт",
 "Сб",
 "Нд");

// First day of the week. "0" means display Sunday first, "1" means display
// Monday first, etc.
Calendar._FD = 1;

// full month names
Calendar._MN = new Array
("Січнь",
 "Лютий",
 "Березень",
 "Квітень",
 "Травень",
 "Червень",
 "Липень",
 "Серпень",
 "Вересень",
 "Жовтень",
 "Листопад",
 "Грудень");

// short month names
Calendar._SMN = new Array
("Січ",
 "Лют",
 "Бер",
 "Кві",
 "Трв",
 "Чер",
 "Лип",
 "Сер",
 "Вер",
 "Жов",
 "Лст",
 "Гру");

// tooltips
Calendar._TT = {};
Calendar._TT["INFO"] = "Про календар";

Calendar._TT["ABOUT"] =
"DHTML Date/Time Selector\n" +
"(c) dynarch.com 2002-2005 / Author: Mihai Bazon\n" + // don't translate this this ;-)
"For latest version visit: http://www.dynarch.com/projects/calendar/\n" +
"Distributed under GNU LGPL.  See http://gnu.org/licenses/lgpl.html for details." +
"\n\n" +
"Як вибрати дату:\n" +
"- Застосовуйте \xab, \xbb кнопки для вибору року\n" +
"- Застосовуйте " + String.fromCharCode(0x2039) + ", " + String.fromCharCode(0x203a) + " кнопки для вибору місяця\n" +
"- Затримайте ці кнопки нажатими, щоб з'явилося меню швидкого вибору.";
Calendar._TT["ABOUT_TIME"] = "\n\n" +
"Як вибрати час:\n" +
"- При клацанні на годинах чи хвилинах вони збільшаться\n" +
"- або Shift і клацнути - зменшити \n" +
"- або натиснути і рухати мишкою вправо/вліво, і вони будуть швидше змінюватися. ";

Calendar._TT["PREV_YEAR"] = "Попередній рік (утримувати для меню)";
Calendar._TT["PREV_MONTH"] = "Попередній місяць (утримувати для меню)";
Calendar._TT["GO_TODAY"] = "Сьогодні";
Calendar._TT["NEXT_MONTH"] = "Наступний місяць (утримувати для меню)";
Calendar._TT["NEXT_YEAR"] = "Наступний рік (утримувати для меню)";
Calendar._TT["SEL_DATE"] = "Виберіть дату";
Calendar._TT["DRAG_TO_MOVE"] = "Тягніть, щоб перемістити";
Calendar._TT["PART_TODAY"] = " (сьогодні)";

// the following is to inform that "%s" is to be the first day of week
// %s will be replaced with the day name.
Calendar._TT["DAY_FIRST"] = "Показати %s перш(і/их)";

// This may be locale-dependent.  It specifies the week-end days, as an array
// of comma-separated numbers.  The numbers are from 0 to 6: 0 means Sunday, 1
// means Monday, etc.
Calendar._TT["WEEKEND"] = "0,6";

Calendar._TT["CLOSE"] = "Закрити";
Calendar._TT["TODAY"] = "Сьогодні";
Calendar._TT["TIME_PART"] = "(Shift-)Клацнути щоб змінити значення";

// date formats
Calendar._TT["DEF_DATE_FORMAT"] = "%Y-%m-%d";
Calendar._TT["TT_DATE_FORMAT"] = "%a, %b %e";

Calendar._TT["WK"] = "тж";
Calendar._TT["TIME"] = "Час:";
