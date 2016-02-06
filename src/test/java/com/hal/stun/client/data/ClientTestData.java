package com.hal.stun.client.data;

import static com.hal.stun.message.StunMessageUtils.MASK;

public class ClientTestData {

  public static final int[] UNCONVERTED_BASIC_REQUEST_IPV4 = {
    0x00, 0x01, 0x00, 0x58,  //    Request type and message length
    0x21, 0x12, 0xa4, 0x42,  //    Magic cookie
    0xb7, 0xe7, 0xa7, 0x01,  // }
    0xbc, 0x34, 0xd6, 0x86,  // }  Transaction ID
    0xfa, 0x87, 0xdf, 0xae,  // }
    0x80, 0x22, 0x00, 0x10,  //    SOFTWARE attribute header
    0x53, 0x54, 0x55, 0x4e,  // }
    0x20, 0x74, 0x65, 0x73,  // }  User-agent...
    0x74, 0x20, 0x63, 0x6c,  // }  ...name
    0x69, 0x65, 0x6e, 0x74,  // }
    0x00, 0x24, 0x00, 0x04,  //    PRIORITY attribute header
    0x6e, 0x00, 0x01, 0xff,  //    ICE priority value
    0x80, 0x29, 0x00, 0x08,  //    ICE-CONTROLLED attribute header
    0x93, 0x2f, 0xf9, 0xb1,  // }  Pseudo-random tie breaker...
    0x51, 0x26, 0x3b, 0x36,  // }   ...for ICE control
    0x00, 0x06, 0x00, 0x09,  //     USERNAME attribute header
    0x65, 0x76, 0x74, 0x6a,  // }
    0x3a, 0x68, 0x36, 0x76,  // }  Username (9 bytes) and padding (3 bytes)
    0x59, 0x20, 0x20, 0x20,  // }
    0x00, 0x08, 0x00, 0x14,  //    MESSAGE-INTEGRITY attribute header
    0x9a, 0xea, 0xa7, 0x0c,  // }
    0xbf, 0xd8, 0xcb, 0x56,  // }
    0x78, 0x1e, 0xf2, 0xb5,  // }  HMAC-SHA1 fingerprint
    0xb2, 0xd3, 0xf2, 0x49,  // }
    0xc1, 0xb5, 0x71, 0xa2,  // }
    0x80, 0x28, 0x00, 0x04,  //    FINGERPRINT attribute header
    0xe5, 0x7a, 0x3b, 0xcf   //    CRC32 fingerprint
  };
  public static final byte[] BASIC_REQUEST_IPV4 = convertToBytes(UNCONVERTED_BASIC_REQUEST_IPV4);

  public static final int[] UNCONVERTED_BASIC_RESPONSE_IPV4 = {
    0x01, 0x01, 0x00, 0x3c, // Response type and message length
    0x21, 0x12, 0xa4, 0x42, // Magic cookie
    0xb7, 0xe7, 0xa7, 0x01, // }
    0xbc, 0x34, 0xd6, 0x86, // }  Transaction ID
    0xfa, 0x87, 0xdf, 0xae, // }
    0x80, 0x22, 0x00, 0x0b, // SOFTWARE attribute header
    0x74, 0x65, 0x73, 0x74, // }
    0x20, 0x76, 0x65, 0x63, // }  UTF-8 server name
    0x74, 0x6f, 0x72, 0x20, // }
    0x00, 0x20, 0x00, 0x08, // XOR-MAPPED-ADDRESS attribute header
    0x00, 0x01, 0xa1, 0x47, // Address family (IPv4) and xor'd mapped port number
    0xe1, 0x12, 0xa6, 0x43, // Xor'd mapped IPv4 address
    0x00, 0x08, 0x00, 0x14, // MESSAGE-INTEGRITY attribute header
    0x2b, 0x91, 0xf5, 0x99, // }
    0xfd, 0x9e, 0x90, 0xc3, // }
    0x8c, 0x74, 0x89, 0xf9, // }  HMAC-SHA1 fingerprint
    0x2a, 0xf9, 0xba, 0x53, // }
    0xf0, 0x6b, 0xe7, 0xd7, // }
    0x80, 0x28, 0x00, 0x04, // FINGERPRINT attribute header
    0xc0, 0x7d, 0x4c, 0x96 // CRC32 fingerprint
  };
  public static final byte[] BASIC_RESPONSE_IPV4 = convertToBytes(UNCONVERTED_BASIC_RESPONSE_IPV4);

  public static byte[] convertToBytes(int[] unconverted) {
    byte[] converted = new byte[unconverted.length];
    for (int i = 0; i < unconverted.length; i++) {
      converted[i] = (byte) (MASK & unconverted[i]);
    }
    return converted;
  }
}