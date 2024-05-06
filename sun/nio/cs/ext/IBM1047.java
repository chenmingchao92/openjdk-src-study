/*
 * Copyright (c) 2008, 2013, Oracle and/or its affiliates. All rights reserved.
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

package sun.nio.cs.ext;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import sun.nio.cs.StandardCharsets;
import sun.nio.cs.SingleByte;
import sun.nio.cs.HistoricallyNamedCharset;
import static sun.nio.cs.CharsetMapping.*;

public class IBM1047 extends Charset implements HistoricallyNamedCharset
{
    public IBM1047() {
        super("IBM1047", ExtendedCharsets.aliasesFor("IBM1047"));
    }

    public String historicalName() {
        return "Cp1047";
    }

    public boolean contains(Charset cs) {
        return (cs instanceof IBM1047);
    }

    public CharsetDecoder newDecoder() {
        return new SingleByte.Decoder(this, b2c);
    }

    public CharsetEncoder newEncoder() {
        return new SingleByte.Encoder(this, c2b, c2bIndex);
    }

    private final static String b2cTable = 
        "\u00D8\u0061\u0062\u0063\u0064\u0065\u0066\u0067" +      // 0x80 - 0x87
        "\u0068\u0069\u00AB\u00BB\u00F0\u00FD\u00FE\u00B1" +      // 0x88 - 0x8f
        "\u00B0\u006A\u006B\u006C\u006D\u006E\u006F\u0070" +      // 0x90 - 0x97
        "\u0071\u0072\u00AA\u00BA\u00E6\u00B8\u00C6\u00A4" +      // 0x98 - 0x9f
        "\u00B5\u007E\u0073\u0074\u0075\u0076\u0077\u0078" +      // 0xa0 - 0xa7
        "\u0079\u007A\u00A1\u00BF\u00D0\u005B\u00DE\u00AE" +      // 0xa8 - 0xaf
        "\u00AC\u00A3\u00A5\u00B7\u00A9\u00A7\u00B6\u00BC" +      // 0xb0 - 0xb7
        "\u00BD\u00BE\u00DD\u00A8\u00AF\u005D\u00B4\u00D7" +      // 0xb8 - 0xbf
        "\u007B\u0041\u0042\u0043\u0044\u0045\u0046\u0047" +      // 0xc0 - 0xc7
        "\u0048\u0049\u00AD\u00F4\u00F6\u00F2\u00F3\u00F5" +      // 0xc8 - 0xcf
        "\u007D\u004A\u004B\u004C\u004D\u004E\u004F\u0050" +      // 0xd0 - 0xd7
        "\u0051\u0052\u00B9\u00FB\u00FC\u00F9\u00FA\u00FF" +      // 0xd8 - 0xdf
        "\\\u00F7\u0053\u0054\u0055\u0056\u0057\u0058" +      // 0xe0 - 0xe7
        "\u0059\u005A\u00B2\u00D4\u00D6\u00D2\u00D3\u00D5" +      // 0xe8 - 0xef
        "\u0030\u0031\u0032\u0033\u0034\u0035\u0036\u0037" +      // 0xf0 - 0xf7
        "\u0038\u0039\u00B3\u00DB\u00DC\u00D9\u00DA\u009F" +      // 0xf8 - 0xff
        "\u0000\u0001\u0002\u0003\u009C\t\u0086\u007F" +      // 0x00 - 0x07
        "\u0097\u008D\u008E\u000B\f\r\u000E\u000F" +      // 0x08 - 0x0f
        "\u0010\u0011\u0012\u0013\u009D\n\b\u0087" +      // 0x10 - 0x17
        "\u0018\u0019\u0092\u008F\u001C\u001D\u001E\u001F" +      // 0x18 - 0x1f
        "\u0080\u0081\u0082\u0083\u0084\u0085\u0017\u001B" +      // 0x20 - 0x27
        "\u0088\u0089\u008A\u008B\u008C\u0005\u0006\u0007" +      // 0x28 - 0x2f
        "\u0090\u0091\u0016\u0093\u0094\u0095\u0096\u0004" +      // 0x30 - 0x37
        "\u0098\u0099\u009A\u009B\u0014\u0015\u009E\u001A" +      // 0x38 - 0x3f
        "\u0020\u00A0\u00E2\u00E4\u00E0\u00E1\u00E3\u00E5" +      // 0x40 - 0x47
        "\u00E7\u00F1\u00A2\u002E\u003C\u0028\u002B\u007C" +      // 0x48 - 0x4f
        "\u0026\u00E9\u00EA\u00EB\u00E8\u00ED\u00EE\u00EF" +      // 0x50 - 0x57
        "\u00EC\u00DF\u0021\u0024\u002A\u0029\u003B\u005E" +      // 0x58 - 0x5f
        "\u002D\u002F\u00C2\u00C4\u00C0\u00C1\u00C3\u00C5" +      // 0x60 - 0x67
        "\u00C7\u00D1\u00A6\u002C\u0025\u005F\u003E\u003F" +      // 0x68 - 0x6f
        "\u00F8\u00C9\u00CA\u00CB\u00C8\u00CD\u00CE\u00CF" +      // 0x70 - 0x77
        "\u00CC\u0060\u003A\u0023\u0040\'\u003D\"" ;      // 0x78 - 0x7f


    private final static char[] b2c = b2cTable.toCharArray();
    private final static char[] c2b = new char[0x100];
    private final static char[] c2bIndex = new char[0x100];

    static {
        char[] b2cMap = b2c;
        char[] c2bNR = null;
        SingleByte.initC2B(b2cMap, c2bNR, c2b, c2bIndex);
    }
}
