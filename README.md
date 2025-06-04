# Gerçek Zamanlı Java Sözdizimi Vurgulayıcı (Real-Time Java Syntax Highlighter)

## 📑 İçindekiler
- 📌 Proje Hakkında
- 🛠 Kullanılan Teknolojiler 
- 🧠 Sözdizimi Analizi Süreci 
- 🔍 Lexical Analyzer 
- 🌳 Parser (Top-Down Yöntemi)
- 🎨 Gerçek Zamanlı Renklendirme  
- 🖥 Grafik Arayüz (GUI)
- 🎥 Tanıtım Videosu

---

## 📌 Proje Hakkında

Bu proje, Java dili için gerçek zamanlı çalışan bir **sözdizimi vurgulayıcıdır**. Girdi olarak alınan kodu **lexical analysis (sözlük analizi)** ve **syntax analysis (sözdizimi analizi)** adımlarıyla işler. Sözdizimi kontrolü, önceden tanımlanmış bir **bağlamdan bağımsız dilbilgisi (CFG)** ile yapılır. GUI üzerinde yazılan kod, eş zamanlı olarak analiz edilip renklendirilir ve hata mesajları gerçek zamanlı olarak gösterilir.

> Projede herhangi bir hazır kütüphane (syntax highlighting kütüphaneleri gibi) kullanılmamıştır.

---

## 🛠 Kullanılan Teknolojiler

- **Programlama Dili**: Java  
- **Arayüz**: Swing (`JTextPane`, `StyledDocument`)  
- **Yapı**: Kendi yazılmış Lexer, Parser, ve Token sınıfları  
- **Derleyici Tipi**: Top-Down Parser (Recursive Descent)

---

## 🧠 Sözdizimi Analizi Süreci

Sözdizimi analizi iki ana bileşenden oluşur:

- **Lexical Analyzer (Lexer)**: Girdiyi token’lara ayırır.
- **Parser**: Token akışını CFG’ye göre analiz eder ve anlamlı bir yapı olup olmadığını kontrol eder.

---

## 🔍 Lexical Analyzer

Lexical analiz, `Lexer.java` sınıfı ile gerçekleştirilmiştir. Girdi karakterleri tek tek okunarak uygun token’lara ayrılır.

### Tanınan Token Türleri:
- `KEYWORD`: class, int, if, else, void vb.
- `IDENTIFIER`: değişken ve metot isimleri
- `SYMBOL`: { } ; ( ) = gibi semboller
- `NUMBER`: Sayısal ifadeler (ör: 10, 42)
- `STRING`: Metinler (ör: "Merhaba")
- `EOF`: Girdi sonu

Yorumlayıcı, `char` düzeyinde kontrol yaparak karakter bazlı bir **durum diyagramı**na göre işlemi yürütür.

---

## 🌳 Parser (Top-Down Yöntemi)

Parser sınıfı, Recursive Descent yöntemine göre çalışır. Tanımlanan CFG’ye göre gelen token’ları sırayla kontrol eder.

### Kullanılan Dilbilgisi Kuralları (Özet CFG):
```ebnf
program         → classDecl
classDecl       → 'class' IDENTIFIER '{' classBody '}'
classBody       → (methodDecl | varDecl)*
methodDecl      → 'void' IDENTIFIER '(' ')' '{' statement* '}'
varDecl         → 'int' IDENTIFIER '=' NUMBER ';'
statement       → varDecl | ifStmt | printStmt
ifStmt          → 'if' '(' expression ')' '{' statement* '}' ('else' '{' statement* '}')?
printStmt       → 'System' '.' 'out' '.' 'println' '(' (STRING | IDENTIFIER) ')' ';'
expression      → IDENTIFIER comparisonOp NUMBER
comparisonOp    → '>' | '<' | '==' | '!=' | '>=' | '<='
```

---

## 🎨 Gerçek Zamanlı Renklendirme

GUI üzerinden yazılan her karakter sonrası döküman yeniden analiz edilir.  
`StyledDocument` kullanılarak ilgili token'lara uygun renkler atanır.

### Kullanılan Renkler:

| Token Türü                  | Renk     |
|----------------------------|----------|
| Anahtar Kelime (`KEYWORD`) | Mavi     |
| Tanımlayıcı (`IDENTIFIER`) | Siyah    |
| Sayılar (`NUMBER`)         | Turuncu  |
| Metinler (`STRING`)        | Yeşil    |
| Semboller (`SYMBOL`)       | Gri      |

Gerçek zamanlı olarak **hatalı satırların altına hata mesajı** yazılır.

---

## 🖥 Grafik Arayüz (GUI)

Swing kütüphanesi kullanılarak oluşturulmuştur:

- **Kod yazma alanı**: `JTextPane`  
- **Renkli sözdizimi**: `StyledDocument`  
- **Hataların gösterimi**: `JLabel` veya alt panel  

Her metin değişiminde arkaplanda **lexer** ve **parser** çalışarak yeni durum anlık olarak hesaplanır.

---

## 🎥 Tanıtım Videosu

Projenin nasıl çalıştığını gösteren demo videosuna aşağıdan ulaşabilirsiniz:  
🔗 [Tanıtım Videosu Bağlantısı](https://ornek-link.com)

