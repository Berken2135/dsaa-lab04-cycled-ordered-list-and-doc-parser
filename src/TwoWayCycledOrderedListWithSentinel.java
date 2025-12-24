import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

// Sıralı, çift yönlü ve döngüsel bağlı liste sınıfı (sentinel düğüm içerir)
public class TwoWayCycledOrderedListWithSentinel<E> implements IList<E> {

    // İç sınıf: Liste düğümü (node) yapısı
    private class Element {
        E object;                // Düğümde saklanan veri
        Element next = null;     // Sonraki düğüme referans
        Element prev = null;     // Önceki düğüme referans

        // Tek parametreli constructor
        public Element(E e) {
            this.object = e;
        }

        // Üç parametreli constructor; nesne, sonraki ve önceki düğümleri alır
        public Element(E e, Element next, Element prev) {
            this.object = e;
            this.next = next;
            this.prev = prev;
        }

        // Bu düğümden sonra verilen elem düğümünü ekler
        public void addAfter(Element elem) {
            elem.next = this.next;
            elem.prev = this;
            this.next.prev = elem;
            this.next = elem;
        }

        // Bu düğümü listeden çıkartır
        public void remove() {
            this.prev.next = this.next;
            this.next.prev = this.prev;
        }
    }

    private Element sentinel; // Listenin başı ve sonunu temsil eden özel düğüm
    private int size;         // Listedeki eleman sayısı

    // İç sınıf: Basit Iterator (sadece ileri gider)
    private class InnerIterator implements Iterator<E> {
        private Element current;

        public InnerIterator() {
            current = sentinel.next; // Başlangıç olarak ilk elemana ayarla
        }

        @Override
        public boolean hasNext() {
            return current != sentinel; // Sentinel'e gelmediyse devam edebilir
        }

        @Override
        public E next() {
            if (!hasNext())
                throw new NoSuchElementException(); // Eğer daha eleman yoksa hata ver
            E temp = current.object;
            current = current.next; // Sonraki elemana geç
            return temp;
        }
    }

    // İç sınıf: İki yönlü gezinmeyi sağlayan ListIterator
    private class InnerListIterator implements ListIterator<E> {
        private Element current;
        private int index;

        public InnerListIterator() {
            current = sentinel.next;
            index = 0;
        }

        @Override
        public boolean hasNext() {
            return current != sentinel;
        }

        @Override
        public E next() {
            if (!hasNext())
                throw new NoSuchElementException();
            E temp = current.object;
            current = current.next;
            index++;
            return temp;
        }

        @Override
        public boolean hasPrevious() {
            return current.prev != sentinel;
        }

        @Override
        public E previous() {
            if (!hasPrevious())
                throw new NoSuchElementException();
            current = current.prev;
            index--;
            return current.object;
        }

        @Override
        public int nextIndex() {
            throw new UnsupportedOperationException(); // Kullanılmıyor
        }

        @Override
        public int previousIndex() {
            throw new UnsupportedOperationException(); // Kullanılmıyor
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException(); // Kullanılmıyor
        }

        @Override
        public void set(E e) {
            throw new UnsupportedOperationException(); // Kullanılmıyor
        }

        @Override
        public void add(E e) {
            throw new UnsupportedOperationException(); // Kullanılmıyor
        }
    }

    // Constructor: Sentinel düğüm oluşturulur ve kendisini gösterir (boş liste)
    public TwoWayCycledOrderedListWithSentinel() {
        sentinel = new Element(null);  // Veri tutmaz, sadece başlangıç/son
        sentinel.next = sentinel;      // Başlangıçta kendisini işaret eder
        sentinel.prev = sentinel;
        size = 0;
    }

    // Sıralı ekleme metodu
    @Override
    public boolean add(E e) {
        Element newElem = new Element(e);
        Element current = sentinel.next;

        // Doğru konumu bulmak için liste boyunca ilerle
        while (current != sentinel && ((Comparable<E>) current.object).compareTo(e) <= 0) {
            current = current.next;
        }

        // Yeni düğümü bulunduğu yere ekle
        current.prev.addAfter(newElem);
        size++;
        return true;
    }

    // Belirli index'teki düğümü döndürür
    private Element getElement(int index) {
        if (index < 0 || index >= size)
            throw new NoSuchElementException();
        Element current = sentinel.next;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current;
    }

    // Objeye göre düğümü bulur
    private Element getElement(E obj) {
        Element current = sentinel.next;
        while (current != sentinel) {
            if (current.object.equals(obj)) {
                return current;
            }
            current = current.next;
        }
        return null;
    }

    @Override
    public void add(int index, E element) {
        throw new UnsupportedOperationException(); // Liste sıralı, index ile ekleme desteklenmiyor
    }

    @Override
    public void clear() {
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
        size = 0;
    }

    @Override
    public boolean contains(E element) {
        return getElement(element) != null;
    }

    @Override
    public E get(int index) {
        return getElement(index).object;
    }

    @Override
    public E set(int index, E element) {
        throw new UnsupportedOperationException(); // Sıralı liste olduğu için desteklenmez
    }

    @Override
    public int indexOf(E element) {
        Element current = sentinel.next;
        int idx = 0;
        while (current != sentinel) {
            if (current.object.equals(element)) {
                return idx;
            }
            idx++;
            current = current.next;
        }
        return -1;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public Iterator<E> iterator() {
        return new InnerIterator();
    }

    @Override
    public ListIterator<E> listIterator() {
        return new InnerListIterator();
    }

    @Override
    public E remove(int index) {
        Element target = getElement(index);
        target.remove();
        size--;
        return target.object;
    }

    @Override
    public boolean remove(E e) {
        Element target = getElement(e);
        if (target != null) {
            target.remove();
            size--;
            return true;
        }
        return false;
    }

    @Override
    public int size() {
        return size;
    }

    // İki sıralı listeyi birleştirir (merge işlemi)
    public void add(TwoWayCycledOrderedListWithSentinel<E> other) {
        if (other == this) return; // Aynı listeyle birleşmeye gerek yok

        Element p = this.sentinel.next;   // Bu listenin başı
        Element q = other.sentinel.next;  // Diğer listenin başı
        Element merged = this.sentinel;   // Birleştirme işlemini buradan başlat

        // Sıralı birleşim
        while (p != this.sentinel && q != other.sentinel) {
            if (((Comparable<E>) p.object).compareTo(q.object) <= 0) {
                merged.next = p;
                p.prev = merged;
                merged = p;
                p = p.next;
            } else {
                merged.next = q;
                q.prev = merged;
                merged = q;
                q = q.next;
            }
        }

        // Kalan elemanları sona ekle
        while (p != this.sentinel) {
            merged.next = p;
            p.prev = merged;
            merged = p;
            p = p.next;
        }
        while (q != other.sentinel) {
            merged.next = q;
            q.prev = merged;
            merged = q;
            q = q.next;
        }

        // Listenin sonunu sentinel ile bağla
        merged.next = this.sentinel;
        this.sentinel.prev = merged;

        // Boyut güncelle
        this.size += other.size;

        // Diğer listeyi boşalt
        other.sentinel.next = other.sentinel;
        other.sentinel.prev = other.sentinel;
        other.size = 0;
    }

    // Listede belirtilen elemandan tümünü siler
    public void removeAll(E e) {
        Element current = sentinel.next;
        while (current != sentinel) {
            if (current.object.equals(e)) {
                Element toRemove = current;
                current = current.next; // Önce ilerle, sonra sil
                toRemove.remove();
                size--;
            } else {
                current = current.next;
            }
        }
    }
}
