[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/2Jhf9klJ)
[![Work in MakeCode](https://classroom.github.com/assets/work-in-make-code-8824cc13a1a3f34ffcd245c82f0ae96fdae6b7d554b6539aec3a03a70825519c.svg)](https://classroom.github.com/online_ide?assignment_repo_id=18379598&assignment_repo_type=AssignmentRepo)
# Ödev 4

Bu ödevdeki göreviniz `Personal Finance` uygulaması için backend bir uygulama geliştirmek. 

Görevleriniz:

- Servis katmanının geliştirmesini tamamlamak.
- Servis katmanının minimum %80 code coverage ile unit test yazmayı tamamlamak.
- Controller katmanının geliştirmesini tamamlamak.
- Controller katmanının minimum %80 code coverage ile unit test yazmayı tamamlamak.
- Repository katmanının geliştirmesini tamamlamak.

> **ÖNEMLİ UYARI:** Coverage durumunuzu mvn verify çalıştırdıktan sonra console çıktısından aşağıdaki uyarı

> controllers alt projesi göreceğiniz UYARI

```
[WARNING] Rule violated for bundle controllers: lines covered ratio is 0.44, but expected minimum is 0.80
```

> services alt projesi göreceğiniz UYARI

```
[WARNING] Rule violated for bundle services: lines covered ratio is 0.00, but expected minimum is 0.80
```

> **ÖNERİ**: Local'inizde `services` ve `controllers` alt projelerinde yer alan `<build>` konfigurasyonunu geçici olarak kapatıp implementasyon ve unit test'lerini tamamlamaya öncelik vermeniz ödevi hızlı bitirmenizi sağlayacaktır.

Puanlama:

- `mvn install` komutunun başarılı çalışması => 10 puan
- `mvn verify` komutunun başarılı çalışması => 90 puan

Kurallar:

- Hiçbir şart altında projenin klasör yapısı bozulmayacak.
	- İstisna olarak `service` alt projesindeki yapacağınız interface implementasyonları için java paketi oluşturabilirsiniz.
 	- Java paketi oluşturmakta sınır bulunmamaktadır. Ama mümkün olduğunca yapıyı korumaya çalışın. Çünkü pom.xml'de bazı kurallar tanımlandı. 
- `DOKUNULMAMASI` gereken paketler ve dosyalar:
	- `classroom.yml` dosyası. *Değiştirmeniz halinde 0 alırsınız.*
	- Tüm `pom.xml` dosyaları. Eski haline döndürdüğünüz sürece local'inizde değişiklik yapabilirsiniz. *Değiştirmeniz halinde 0 alırsınız.* (İstisna olarak kök dizinde yer alan `pom.xml`'e kullanacağınız veritabanının driver kütüphanesini ekleyebilirsiniz.)
	- `exception` paketi. *Değiştirmeniz halinde 0 alırsınız.*
