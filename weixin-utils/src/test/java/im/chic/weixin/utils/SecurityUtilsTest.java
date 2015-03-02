package im.chic.weixin.utils;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * @author huahang
 */
@RunWith(JUnit4.class)
public class SecurityUtilsTest {
    @Test
    public void testSignature() {
        Assert.assertEquals(
                "f091ab3e5d080d03a1f2f1ec4ec13a5c7c41cb90",
                SecurityUtils.calculateSignature(
                        "http://weixin.miwifi.com/device",
                        "sM4AOVdWfPE4DxkXGEs8VI1U8GEP5yvgUfj4K50wFeTYpQNHqJSMxfuitUuGdmO0xtOCig2CkkWouX7c_yCbsg",
                        "123456789",
                        "fdasfdsafdsafdsa"
                )
        );
    }
}
