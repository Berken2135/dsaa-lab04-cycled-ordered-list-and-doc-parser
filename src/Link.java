public class Link implements Comparable<Link> {
    // Link'in referans kısmı (isim)
    public String ref;
    // Link'in ağırlığı
    public int weight;

    // Sadece referans alan constructor; ağırlık varsayılan olarak 1 olur.
    public Link(String ref) {
        if (ref == null) throw new IllegalArgumentException("Reference cannot be null.");
        this.ref = ref;
        this.weight = 1; // Default weight is 1
    }

    // Referans ve ağırlık alan constructor
    public Link(String ref, int weight) {
        if (ref == null) throw new IllegalArgumentException("Reference cannot be null.");
        if (weight <= 0) throw new IllegalArgumentException("Weight must be positive.");
        this.ref = ref;
        this.weight = weight;
    }

    // equals metodu; iki Link nesnesi eşit ise sadece ref alanları aynı olmalıdır.
    @Override
    public boolean equals(Object obj) {
        // Aynı nesne ise true döndür
        if (this == obj) {
            return true;
        }
        // null veya Link türünde değilse false döndür
        if (obj == null || !(obj instanceof Link)) {
            return false;
        }
        Link other = (Link) obj;
        // Sadece ref alanlarını karşılaştır (küçük harflerle saklandığı varsayılır)
        return this.ref != null && this.ref.equals(other.ref);
    }

    // Link nesnesini "ref(weight)" formatında string olarak döndürür.
    @Override
    public String toString() {
        return ref + "(" + weight + ")";
    }

    // compareTo metodu; iki Link'i sadece ref alanı üzerinden karşılaştırır.
    @Override
    public int compareTo(Link another) {
        // Link'ler, ref alanına göre sıralanır.
        return this.ref.compareTo(another.ref);
    }
}
