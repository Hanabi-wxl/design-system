import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.*;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.junit.jupiter.api.Test;

import java.io.FileOutputStream;
import java.io.IOException;

public class PdfTest {

    @Test
    public void test() {
        String templatePath = "C:\\Users\\21481\\Desktop\\1.pdf";
        // 生成的新文件路径
        String newPDFPath = "C:\\Users\\21481\\Desktop\\2.pdf";
        PdfReader reader;
        FileOutputStream out;
        ByteArrayOutputStream bos;
        PdfStamper stamper;
        try {
            out = new FileOutputStream(newPDFPath);// 输出流
            reader = new PdfReader(templatePath);// 读取pdf模板
            bos = new ByteArrayOutputStream();
            stamper = new PdfStamper(reader, bos);
            AcroFields form = stamper.getAcroFields();
            String[] str = {"男","男","男","男","男","男","男","男","男","男","男","男","男","男","男"};
            int i = 0;
            java.util.Iterator<String> it = form.getFields().keySet().iterator();
            while (it.hasNext()) {
                String name = it.next().toString();
                System.out.println(name);
                form.setField(name, str[i++]);
            }
            stamper.setFormFlattening(true);// 如果为false那么生成的PDF文件还能编辑，一定要设为true
            stamper.close();
            Document doc = new Document();
            PdfCopy copy = new PdfCopy(doc, out);
            doc.open();

            for (int j = 1; j <= 3; j++) {
                PdfImportedPage importPage = copy.getImportedPage(new PdfReader(bos.toByteArray()), j);
                copy.addPage(importPage);
            }

            doc.close();
            System.out.println(11111111);
        } catch (IOException e) {
            System.out.println(1);
        } catch (DocumentException e) {
            System.out.println(2);
        }
    }
}
