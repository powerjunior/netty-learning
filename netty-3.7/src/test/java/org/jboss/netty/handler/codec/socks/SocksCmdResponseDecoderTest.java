/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package org.jboss.netty.handler.codec.socks;

import org.jboss.netty.handler.codec.embedder.DecoderEmbedder;
import org.jboss.netty.logging.InternalLogger;
import org.jboss.netty.logging.InternalLoggerFactory;
import org.junit.Test;

import static org.junit.Assert.*;

public class SocksCmdResponseDecoderTest {
    private static final InternalLogger logger = InternalLoggerFactory.getInstance(SocksCmdResponseDecoderTest.class);

    private static void testSocksCmdResponseDecoderWithDifferentParams(SocksMessage.CmdStatus cmdStatus,
                    SocksMessage.AddressType addressType) throws Exception {
        logger.debug("Testing cmdStatus: " + cmdStatus + " addressType: " + addressType);
        SocksResponse msg = new SocksCmdResponse(cmdStatus, addressType);
        SocksCmdResponseDecoder decoder = new SocksCmdResponseDecoder();
        DecoderEmbedder<SocksResponse> embedder = new DecoderEmbedder<SocksResponse>(decoder);
        SocksCommonTestUtils.writeMessageIntoEmbedder(embedder, msg);
        if (addressType == SocksMessage.AddressType.UNKNOWN) {
            assertTrue(embedder.poll() instanceof UnknownSocksResponse);
        } else {
            msg = embedder.poll();
            assertEquals(((SocksCmdResponse) msg).getCmdStatus(), cmdStatus);
        }
        assertNull(embedder.poll());
    }

    @Test
    public void testSocksCmdResponseDecoder() throws Exception {
        for (SocksMessage.CmdStatus cmdStatus: SocksMessage.CmdStatus.values()) {
            for (SocksMessage.AddressType addressType: SocksMessage.AddressType.values()) {
                testSocksCmdResponseDecoderWithDifferentParams(cmdStatus, addressType);
            }
        }
    }
}
