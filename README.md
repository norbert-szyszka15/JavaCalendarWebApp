# JavaCalendarWebApp
![title](https://github.com/user-attachments/assets/e5088a41-b081-4972-a03a-a3442f5887a3)

---

### **Budowanie i uruchamianie backendu aplikacji**
Instrukcja zbudowania i uruchomienia backendu:

1. Sklonuj repozytorium Git na swój komputer:
   ```bash
   git clone https://github.com/norbert-szyszka15/JavaCalendarWebApp.git
   ```
2. Przejdź do katalogu roboczego aplikacji i zbuduj projekt:
   ```bash
   cd JavaCalendarWebApp && docker compose build
   ```
3. Uruchom zbudowane kontenery:
   ```bash
   docker compose up
   ```

---

### **`DataInitializer.java` i pierwsze dane użytkowników**
Przy pierwszym uruchomieniu backendu aplikacji, po usunięciu odpowiednich danych z tabeli `users` lub w przypadku nieposiadania w niej jednego użytkownika z sygnaturą `ADMIN` i jednego użytkownika z sygnaturą `USER`, skrypt zawarty w pliku `DataInitializer.java` utworzy bazowych użytkowników, czyli kolejno:
- administratora `ADMIN`, `USER` o domyślnej nazwie użytkownika `admin` i haśle `admin123` (można to zmodyfikować w ciele skrypu),
- użytkownika standardowego `USER` o domyślnej nazwie użytkownika `user` i haśle `user123` (można to zmodyfikować w ciele skrypu).

![main_users](https://github.com/user-attachments/assets/5d9c5b4a-de6e-4f54-95c5-ade46f0bd4a4)

---

### **Diagram ERD bazy danych**
Diagram ERD bazy danych, na której działa aplikacja, przedstawia się jak poniżej:
![java_calendar_web_app_ERD](https://github.com/user-attachments/assets/7dca8d95-4463-4153-b7bc-a7ebee6895c2)

---

### **Wzorce projektowe zastosowane w aplikacji**
1. **MVC** - podział projektu na warstwy Web, logiczne i dostępu do danych,
2. **Facade** - warstwy serwisowe dla każdej z encji i odpowiadającego jej kontrolera,
3. **Dependency Injection** - w konstruktorach wszystkich klas użyto klas oznaczonch jako `@Component`, `@Service`, `@Repository` lub `@Controller`,
4. **Singleton** - każdy bean w Spring domyślnie jest singletonem.

---

### **Podział na użytkowników i Spring Security**
W aplikacji można wyróżnić dwie główne role - `ADMIN` oraz `USER` (dany użytkownik może jednocześnie przybierać obie te role). Kontrolery zbudowane dla każdej z encji `Calendar`, `User`, `Event` i `Task` zapewniają, że zwykły użytkownik nie może wykonywać poleceń przewidzianych jedynie dla administratora. Przykładowo, poniższa metoda zapewnia, że profil użytkownika (a więc miejscie, w którym wyświetlone zostanie m.in. hashowane hasło użytkownika) wyświetlić może jedynie administrator aplikacji.
```java
@GetMapping
@Operation(summary = "Get user profile", description = "Retrieve the profile information of the currently authenticated user.")
@PreAuthorize("hasAnyRole('ADMIN')")
public List<User> getAllUsers() {
  return userService.findAll();
}
```
![user_restriction](https://github.com/user-attachments/assets/792a5bbb-3e39-49d1-a68f-e4f49df4f5a0)

---

### **Testy jednostkowe i narzędzie JaCoCo**
Aplikacja zawiera także testy jednostkowe o pokryciu kodu przekraczającym wymagane w założeniach projektowych 80%. Testy jednostkowe wykorzystują `@MockMvc` i `@TestContainers`, a stopień pokrycia nimi kodu aplikacji został zweryfikowany przy pomocy narzędzia JaCoCo, które zostało odpowiednio skonfigurowane dla projektu. W celu weryfikacji pokrycia można, znajdując się w katalogu głównym aplikacji, uruchomić komendę `mvn clean verify`. W wyniku tej weryfikacji pod ścieżką `target/site/jacoco` utworzony zostanie plik `index.html`, w którym zawarty jest stopień pokrycia.

Zrzut ekranu z pliku `index.html` wygenerowanego przy pomocy narzędzia JaCoCo:
![jacoco](https://github.com/user-attachments/assets/acc14ea8-66ad-4aaf-86cf-42e3bc998009)

---

