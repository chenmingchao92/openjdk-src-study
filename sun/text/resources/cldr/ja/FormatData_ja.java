/*
 * Copyright (c) 2012, 2024, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

/*
 * COPYRIGHT AND PERMISSION NOTICE
 *
 * Copyright (C) 1991-2012 Unicode, Inc. All rights reserved. Distributed under
 * the Terms of Use in http://www.unicode.org/copyright.html.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of the Unicode data files and any associated documentation (the "Data
 * Files") or Unicode software and any associated documentation (the
 * "Software") to deal in the Data Files or Software without restriction,
 * including without limitation the rights to use, copy, modify, merge,
 * publish, distribute, and/or sell copies of the Data Files or Software, and
 * to permit persons to whom the Data Files or Software are furnished to do so,
 * provided that (a) the above copyright notice(s) and this permission notice
 * appear with all copies of the Data Files or Software, (b) both the above
 * copyright notice(s) and this permission notice appear in associated
 * documentation, and (c) there is clear notice in each modified Data File or
 * in the Software as well as in the documentation associated with the Data
 * File(s) or Software that the data or software has been modified.
 *
 * THE DATA FILES AND SOFTWARE ARE PROVIDED "AS IS", WITHOUT WARRANTY OF ANY
 * KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT OF
 * THIRD PARTY RIGHTS. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR HOLDERS
 * INCLUDED IN THIS NOTICE BE LIABLE FOR ANY CLAIM, OR ANY SPECIAL INDIRECT OR
 * CONSEQUENTIAL DAMAGES, OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE,
 * DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER
 * TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR PERFORMANCE
 * OF THE DATA FILES OR SOFTWARE.
 *
 * Except as contained in this notice, the name of a copyright holder shall not
 * be used in advertising or otherwise to promote the sale, use or other
 * dealings in these Data Files or Software without prior written authorization
 * of the copyright holder.
 */

package sun.text.resources.cldr.ja;

import java.util.ListResourceBundle;

public class FormatData_ja extends ListResourceBundle {
    @Override
    protected final Object[][] getContents() {
        final Object[][] data = new Object[][] {
            { "MonthNames",
                new String[] {
                    "1\u6708",
                    "2\u6708",
                    "3\u6708",
                    "4\u6708",
                    "5\u6708",
                    "6\u6708",
                    "7\u6708",
                    "8\u6708",
                    "9\u6708",
                    "10\u6708",
                    "11\u6708",
                    "12\u6708",
                    "",
                }
            },
            { "DayNames",
                new String[] {
                    "\u65e5\u66dc\u65e5",
                    "\u6708\u66dc\u65e5",
                    "\u706b\u66dc\u65e5",
                    "\u6c34\u66dc\u65e5",
                    "\u6728\u66dc\u65e5",
                    "\u91d1\u66dc\u65e5",
                    "\u571f\u66dc\u65e5",
                }
            },
            { "DayAbbreviations",
                new String[] {
                    "\u65e5",
                    "\u6708",
                    "\u706b",
                    "\u6c34",
                    "\u6728",
                    "\u91d1",
                    "\u571f",
                }
            },
            { "DayNarrows",
                new String[] {
                    "\u65e5",
                    "\u6708",
                    "\u706b",
                    "\u6c34",
                    "\u6728",
                    "\u91d1",
                    "\u571f",
                }
            },
            { "QuarterNames",
                new String[] {
                    "\u7b2c1\u56db\u534a\u671f",
                    "\u7b2c2\u56db\u534a\u671f",
                    "\u7b2c3\u56db\u534a\u671f",
                    "\u7b2c4\u56db\u534a\u671f",
                }
            },
            { "QuarterAbbreviations",
                new String[] {
                    "Q1",
                    "Q2",
                    "Q3",
                    "Q4",
                }
            },
            { "standalone.QuarterAbbreviations",
                new String[] {
                    "Q1",
                    "Q2",
                    "Q3",
                    "Q4",
                }
            },
            { "QuarterNarrows",
                new String[] {
                    "1",
                    "2",
                    "3",
                    "4",
                }
            },
            { "AmPmMarkers",
                new String[] {
                    "\u5348\u524d",
                    "\u5348\u5f8c",
                }
            },
            { "long.Eras",
                new String[] {
                    "\u7d00\u5143\u524d",
                    "\u897f\u66a6",
                }
            },
            { "Eras",
                new String[] {
                    "BC",
                    "AD",
                }
            },
            { "field.era", "\u6642\u4ee3" },
            { "field.year", "\u5e74" },
            { "field.month", "\u6708" },
            { "field.week", "\u9031" },
            { "field.weekday", "\u66dc\u65e5" },
            { "field.dayperiod", "\u5348\u524d/\u5348\u5f8c" },
            { "field.hour", "\u6642" },
            { "field.minute", "\u5206" },
            { "field.second", "\u79d2" },
            { "field.zone", "\u30bf\u30a4\u30e0\u30be\u30fc\u30f3" },
            { "TimePatterns",
                new String[] {
                    "H\u6642mm\u5206ss\u79d2 zzzz",
                    "H:mm:ss z",
                    "H:mm:ss",
                    "H:mm",
                }
            },
            { "DatePatterns",
                new String[] {
                    "y\u5e74M\u6708d\u65e5EEEE",
                    "y\u5e74M\u6708d\u65e5",
                    "yyyy/MM/dd",
                    "yyyy/MM/dd",
                }
            },
            { "DateTimePatterns",
                new String[] {
                    "{1} {0}",
                }
            },
            { "buddhist.long.Eras",
                new String[] {
                    "BC",
                    "\u4ecf\u66a6",
                }
            },
            { "java.time.buddhist.DatePatterns",
                new String[] {
                    "GGGGy\u5e74M\u6708d\u65e5EEEE",
                    "GGGGy\u5e74M\u6708d\u65e5",
                    "Gy/MM/dd",
                    "Gy/MM/dd",
                }
            },
            { "buddhist.DatePatterns",
                new String[] {
                    "GGGGy\u5e74M\u6708d\u65e5EEEE",
                    "GGGGy\u5e74M\u6708d\u65e5",
                    "GGGGy/MM/dd",
                    "GGGGy/MM/dd",
                }
            },
            { "japanese.Eras",
                new String[] {
                    "AD",
                    "\u660e\u6cbb",
                    "\u5927\u6b63",
                    "\u662d\u548c",
                    "\u5e73\u6210",
                    "\u4ee4\u548c",
                }
            },
            { "java.time.japanese.DatePatterns",
                new String[] {
                    "Gy\u5e74M\u6708d\u65e5EEEE",
                    "Gy\u5e74M\u6708d\u65e5",
                    "Gy\u5e74M\u6708d\u65e5",
                    "Gyy/MM/dd",
                }
            },
            { "japanese.DatePatterns",
                new String[] {
                    "GGGGy\u5e74M\u6708d\u65e5EEEE",
                    "GGGGy\u5e74M\u6708d\u65e5",
                    "GGGGy\u5e74M\u6708d\u65e5",
                    "GGGGyy/MM/dd",
                }
            },
            { "roc.Eras",
                new String[] {
                    "\u6c11\u56fd\u524d",
                    "\u6c11\u56fd",
                }
            },
            { "java.time.roc.DatePatterns",
                new String[] {
                    "Gy\u5e74M\u6708d\u65e5EEEE",
                    "Gy\u5e74M\u6708d\u65e5",
                    "Gy/MM/dd",
                    "Gy/MM/dd",
                }
            },
            { "roc.DatePatterns",
                new String[] {
                    "GGGGy\u5e74M\u6708d\u65e5EEEE",
                    "GGGGy\u5e74M\u6708d\u65e5",
                    "GGGGy/MM/dd",
                    "GGGGy/MM/dd",
                }
            },
            { "islamic.MonthNames",
                new String[] {
                    "\u30e0\u30cf\u30c3\u30e9\u30e0",
                    "\u30b5\u30d5\u30a2\u30eb",
                    "\u30e9\u30d3\u30fc\u30fb\u30a6\u30eb\u30fb\u30a2\u30a6\u30ef\u30eb",
                    "\u30e9\u30d3\u30fc\u30fb\u30a6\u30c3\u30fb\u30b5\u30fc\u30cb\u30fc",
                    "\u30b8\u30e5\u30de\u30fc\u30c0\u30eb\u30fb\u30a2\u30a6\u30ef\u30eb",
                    "\u30b8\u30e5\u30de\u30fc\u30c0\u30c3\u30b5\u30fc\u30cb\u30fc",
                    "\u30e9\u30b8\u30e3\u30d6",
                    "\u30b7\u30e3\u30a2\u30d0\u30fc\u30f3",
                    "\u30e9\u30de\u30c0\u30fc\u30f3",
                    "\u30b7\u30e3\u30a6\u30ef\u30fc\u30eb",
                    "\u30ba\u30eb\u30fb\u30ab\u30a4\u30c0",
                    "\u30ba\u30eb\u30fb\u30d2\u30c3\u30b8\u30e3",
                    "",
                }
            },
            { "islamic.MonthAbbreviations",
                new String[] {
                    "\u30e0\u30cf\u30c3\u30e9\u30e0",
                    "\u30b5\u30d5\u30a2\u30eb",
                    "\u30e9\u30d3\u30fc\u30fb\u30a6\u30eb\u30fb\u30a2\u30a6\u30ef\u30eb",
                    "\u30e9\u30d3\u30fc\u30fb\u30a6\u30c3\u30fb\u30b5\u30fc\u30cb\u30fc",
                    "\u30b8\u30e5\u30de\u30fc\u30c0\u30eb\u30fb\u30a2\u30a6\u30ef\u30eb",
                    "\u30b8\u30e5\u30de\u30fc\u30c0\u30c3\u30b5\u30fc\u30cb\u30fc",
                    "\u30e9\u30b8\u30e3\u30d6",
                    "\u30b7\u30e3\u30a2\u30d0\u30fc\u30f3",
                    "\u30e9\u30de\u30c0\u30fc\u30f3",
                    "\u30b7\u30e3\u30a6\u30ef\u30fc\u30eb",
                    "\u30ba\u30eb\u30fb\u30ab\u30a4\u30c0",
                    "\u30ba\u30eb\u30fb\u30d2\u30c3\u30b8\u30e3",
                    "",
                }
            },
            { "calendarname.islamic-civil", "\u592a\u967d\u30a4\u30b9\u30e9\u30e0\u66a6" },
            { "calendarname.islamicc", "\u592a\u967d\u30a4\u30b9\u30e9\u30e0\u66a6" },
            { "calendarname.buddhist", "\u30bf\u30a4\u4ecf\u6559\u66a6" },
            { "calendarname.islamic", "\u30a4\u30b9\u30e9\u30e0\u66a6" },
            { "calendarname.gregorian", "\u897f\u66a6[\u30b0\u30ec\u30b4\u30ea\u30aa\u66a6]" },
            { "calendarname.gregory", "\u897f\u66a6[\u30b0\u30ec\u30b4\u30ea\u30aa\u66a6]" },
            { "calendarname.roc", "\u4e2d\u83ef\u6c11\u56fd\u66a6" },
            { "calendarname.japanese", "\u548c\u66a6" },
            { "DefaultNumberingSystem", "latn" },
            { "latn.NumberElements",
                new String[] {
                    ".",
                    ",",
                    ";",
                    "%",
                    "0",
                    "#",
                    "-",
                    "E",
                    "\u2030",
                    "\u221e",
                    "NaN\uff08\u975e\u6570\uff09",
                }
            },
            { "NumberPatterns",
                new String[] {
                    "#,##0.###",
                    "\u00a4#,##0.00",
                    "#,##0%",
                }
            },
        };
        return data;
    }
}
