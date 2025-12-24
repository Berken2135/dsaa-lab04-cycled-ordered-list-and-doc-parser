import java.util.ListIterator;
import java.util.Scanner;
import java.util.NoSuchElementException;

// Belge (Document) sınıfı: Belge adını ve içerisindeki bağlantıları (linkleri) saklar
public class Document {
    public String name; // Belgenin adı (küçük harfe çevrilmiş şekilde saklanır)
    public TwoWayCycledOrderedListWithSentinel<Link> link; // Belgedeki linkleri tutan sıralı, çift yönlü döngüsel liste

    // Constructor: Belge oluşturulurken adı ve Scanner (veri kaynağı) alınır
    public Document(String name, Scanner scan) {
        this.name = name.toLowerCase(); // Belge adını küçük harfe çevir ve kaydet
        link = new TwoWayCycledOrderedListWithSentinel<>(); // Link listesini oluştur
        load(scan); // Belgedeki linkleri yükle
    }

    // Belgeye linkleri yükleyen metod (eod satırına kadar okur)
    public void load(Scanner scan) {
        while (scan.hasNextLine()) { // Satır oldukça döngü devam eder
            String line = scan.nextLine();
            if (line.equals("eod")) // "eod" ifadesi gelirse yükleme tamamlanır
                break;

            // Satırı boşluklardan ayırarak kelimelere ayır
            String[] tokens = line.split("\\s+");

            for (String token : tokens) {
                // Eğer "link=" ile başlıyorsa (büyük/küçük harf duyarsız)
                if (token.toLowerCase().startsWith("link=")) {
                    String linkStr = token.substring(5); // "link=" kısmını at
                    Link l = createLink(linkStr); // Link nesnesi oluştur
                    if (l != null)
                        link.add(l); // Geçerli ise listeye ekle
                }
            }
        }
    }

    // Belirli bir String ifadenin geçerli bir ID (belge adı) olup olmadığını kontrol eder
    public static boolean isCorrectId(String id) {
        if (id == null || id.length() == 0) // Null veya boşsa geçersiz
            return false;
        char first = id.charAt(0);
        if (!Character.isLetter(first)) // İlk karakter harf olmalı
            return false;
        // Geri kalan karakterler harf, rakam veya '_' olabilir
        for (int i = 1; i < id.length(); i++) {
            char c = id.charAt(i);
            if (!Character.isLetterOrDigit(c) && c != '_')
                return false;
        }
        return true; // Tüm kontroller geçtiyse geçerli
    }

    // Bir link metninden Link nesnesi oluşturur. Örnek: "abc1(34)", "xyz"
    public static Link createLink(String linkText) {
        if (linkText == null || linkText.length() == 0)
            return null;

        String ref; // Link’in yöneldiği belge adı
        int weight = 1; // Varsayılan ağırlık 1
        int parenIndex = linkText.indexOf('('); // '(' varsa ağırlık da vardır

        if (parenIndex != -1) { // Ağırlık varsa
            if (!linkText.endsWith(")")) // ')' ile bitmiyorsa hatalı
                return null;
            ref = linkText.substring(0, parenIndex); // Parantezden önceki kısım
            String weightStr = linkText.substring(parenIndex + 1, linkText.length() - 1); // Parantez içindeki sayı
            try {
                weight = Integer.parseInt(weightStr); // Sayıya çevir
                if (weight <= 0) // Pozitif değilse geçersiz
                    return null;
            } catch (NumberFormatException e) {
                return null; // Sayıya çevrilemiyorsa geçersiz
            }
        } else {
            ref = linkText; // Ağırlık yoksa tümü referanstır
        }

        ref = ref.toLowerCase(); // Küçük harfe çevir
        if (!isCorrectId(ref)) // Geçerli bir id değilse null döndür
            return null;

        if (parenIndex != -1)
            return new Link(ref, weight); // Ağırlıklı link
        else
            return new Link(ref); // Ağırlıksız link
    }

    // Belge içeriğini uygun formatta döndüren metod
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Document: ").append(name); // İlk satırda belge adı
        int count = 0;

        for (Link l : link) { // Listedeki tüm linkleri sırayla yazdır
            if (count % 10 == 0) // Her 10 linkte bir satır atla
                sb.append("\n");
            else
                sb.append(" ");
            sb.append(l.toString()); // Link metnini yaz
            count++;
        }
        return sb.toString();
    }

    // Link listesini ters sırayla yazdıran metod
    public String toStringReverse() {
        StringBuilder sb = new StringBuilder();
        sb.append("Document: ").append(name); // Başlık

        // ListIterator kullanılarak listenin sonuna gidilir
        ListIterator<Link> iter = link.listIterator();
        while (iter.hasNext())
            iter.next(); // İteratörü listenin sonuna götür

        int count = 0;

        // Geriye doğru tüm linkleri yazdır
        while (iter.hasPrevious()) {
            if (count % 10 == 0)
                sb.append("\n");
            else
                sb.append(" ");
            sb.append(iter.previous().toString()); // Önceki elemanı yazdır
            count++;
        }
        return sb.toString();
    }
}
