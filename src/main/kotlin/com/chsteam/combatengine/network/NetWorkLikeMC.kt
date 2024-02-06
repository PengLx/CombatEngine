package com.chsteam.combatengine.network

import io.netty.buffer.ByteBuf
import io.netty.handler.codec.DecoderException
import io.netty.handler.codec.EncoderException
import java.nio.charset.StandardCharsets
import kotlin.experimental.and

fun ByteBuf.writeVarInt(value: Int) : ByteBuf {
    var value = value
    while (value and -128 !== 0) {
        writeByte(value and 127 or 128)
        value = value ushr 7
    }

    writeByte(value)
    return this
}

fun ByteBuf.writeString(string: String): ByteBuf {
    return this.writeString(string, 32767)
}

fun ByteBuf.writeString(string: String, maxLength: Int): ByteBuf {
    return if (string.length > maxLength) {
        val var10002 = string.length
        throw EncoderException("String too big (was $var10002 characters, max $maxLength)")
    } else {
        val bs: ByteArray = string.toByteArray(StandardCharsets.UTF_8)
        val i: Int = toEncodedStringLength(maxLength)
        if (bs.size > i) {
            throw EncoderException("String too big (was " + bs.size + " bytes encoded, max " + i + ")")
        } else {
            this.writeVarInt(bs.size)
            this.writeBytes(bs)
            this
        }
    }
}

private fun toEncodedStringLength(decodedLength: Int): Int {
    return decodedLength * 3
}

fun ByteBuf.readIntArray(): IntArray {
    return this.readIntArray(this.readableBytes())
}

fun ByteBuf.readIntArray(maxSize: Int): IntArray {
    val i: Int = this.readVarInt()
    return if (i > maxSize) {
        throw DecoderException("VarIntArray with size $i is bigger than allowed $maxSize")
    } else {
        val array = IntArray(i)
        for (j in array.indices) {
            array[j] = this.readVarInt()
        }
        array
    }
}

fun ByteBuf.readVarInt(): Int {
    var i = 0
    var j = 0
    var b: Byte
    do {
        b = readByte()
        i = i or (b.toInt() and 127 shl j++ * 7)
        if (j > 5) {
            throw RuntimeException("VarInt too big")
        }
    } while ((b and 128.toByte()) == 128.toByte())
    return i
}

fun ByteBuf.readString(maxLength: Int): String {
    val i: Int = maxLength * 3
    val j: Int = this.readVarInt()
    return if (j > i) {
        throw DecoderException("The received encoded string buffer length is longer than maximum allowed ($j > $i)")
    } else if (j < 0) {
        throw DecoderException("The received encoded string buffer length is less than zero! Weird string!")
    } else {
        val s: String = this.toString(this.readerIndex(), j, StandardCharsets.UTF_8)
        this.readerIndex(this.readerIndex() + j)
        if (s.length > maxLength) {
            val var10002 = s.length
            throw DecoderException("The received string length is longer than maximum allowed ($var10002 > $maxLength)")
        } else {
            s
        }
    }
}