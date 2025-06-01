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

### **Wzorce projektowe zastosowane w aplikacji**
1. **MVC** - podział projektu na warstwy Web, logiczne i dostępu do danych,
2. **Facade** - warstwy serwisowe dla każdej z encji i odpowiadającego jej kontrolera,
3. **Dependency Injection** - w konstruktorach wszystkich klas użyto klas oznaczonch jako `@Component`, `@Service`, `@Repository` lub `@Controller`,
4. **Singleton** - każdy bean w Spring domyślnie jest singletonem.

---

### **Podział na użytkowników**
W aplikacji można wyróżnić dwie główne role - `ADMIN` oraz `USER` (dany użytkownik może jednocześnie przybierać obie te role). Kontrolery zbudowane dla każdej z encji `Calendar`, `User`, `Event` i `Task` zapewniają, że zwykły użytkownik nie może wykonywać poleceń przewidzianych jedynie dla administratora. Przykładowo, poniższa metoda zapewnia, że profil użytkownika (a więc miejscie, w którym wyświetlone zostanie m.in. hashowane hasło użytkownika) wyświetlić może jedynie administrator aplikacji.
```java
@GetMapping
@Operation(summary = "Get user profile", description = "Retrieve the profile information of the currently authenticated user.")
@PreAuthorize("hasAnyRole('ADMIN')")
public List<User> getAllUsers() {
  return userService.findAll();
}

![user_restriction](https://github.com/user-attachments/assets/792a5bbb-3e39-49d1-a68f-e4f49df4f5a0)

```
