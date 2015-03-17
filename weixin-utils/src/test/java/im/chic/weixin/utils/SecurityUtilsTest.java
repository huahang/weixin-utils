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
    @Test
    public void testBase64Decode() {
        String base64 = "eyJoYXJkd2FyZSI6Im1pd2lmaSIsIjVHQ291bnQiOjAsInVzZXJJY29uIjoiaHR0cDovL2x4Y2RuLmRsLmZpbGVzLnhpYW9taS5uZXQvbWZzdjIvYXZhdGFyL3MwMTAvcDAxVDJLZ0FZMkNZLzBZTmVDVEtWZDdJek5aLmpwZyIsInNjb3JlIjo1NCwic2VyaWFsTnVtYmVyIjoiNTY0NzAwMDAwNjM4IiwidG90YWxUcmFmZmljIjoxNDI1MTQwMjI3MCwic21hcnRIb21lIjp7Im1pYm94IjowLCJtaXR2IjowLCJ4aWFvbWktaXIiOjAsImNodWFuZ21pLXBsdWciOjAsInllZWxpZ2h0IjowLCJ6aGltaS1haXJwdXJpZmllciI6MCwiYW50c2NhbSI6MH0sImZ1bmN0aW9uIjp7InNvZ291UHJlbG9hZFBhZ2UiOjAsImFkYmxvY2siOjAsInh1bmxlaURvd25sb2FkQWNjIjowfSwidGltZSI6MTQyMTk3ODk5NiwiZmlsZVN0b3JhZ2UiOnsibXVzaWMiOlswLDEsMjYwOTY1Ml0sIm90aGVycyI6WzYsNTQsMzMyMjYxNzY2NzFdLCJkb2MiOlswLDEsMjUwNzY1MDhdLCJwaG90byI6WzAsMTAzNCwxMTE2MDk0MTk5XSwidmlkZW8iOlsyLDU1LDgxNjgzOTY4NDg0XX0sImRldmljZU5hbWUiOiLukYjujLruhJLugYruhJ3ulJPugKIiLCJkYXlzIjo3LCJ1c2VySWQiOjIzMTI3OTM4LCJtYXhUcmFmZmljRGV2aWNlIjoibWFiYWltaW5nLU9wdGlQbGV4LTkwMTAiLCJhY3RpdmVQZXJjZW50IjoxMDAsInVzZXJOYW1lIjoi6ams55m-6bijIiwiZGV2aWNlTGlzdCI6W3siY29ubmVjdGlvbiI6IndsMSIsInNlbmRCeXRlcyI6MTY0MjYxOCwidGltZSI6MTU3MzAsIm5hbWUiOiJNSTJTLXhpYW9taXNob3VqaSIsInJlY3ZCeXRlcyI6NzU0MzU4NywibWFjIjoiQUM6Rjc6RjM6RTU6NTM6QjAifSx7ImNvbm5lY3Rpb24iOiJldGgwLjEiLCJzZW5kQnl0ZXMiOjE2NTI5MzE4NSwidGltZSI6MjExMDAsIm5hbWUiOiJtYWJhaW1pbmctT3B0aVBsZXgtOTAxMCIsInJlY3ZCeXRlcyI6NDk0MzEwODMyLCJtYWMiOiJCODpDQTozQTpCMTo0MjpBMiJ9XSwibWF4VHJhZmZpY0RldmljZU1hYyI6IkI4OkNBOjNBOkIxOjQyOkEyIiwiYXBwVGltZSI6MCwibWF4T25saW5lQ291bnQiOjJ9";

    }
}
