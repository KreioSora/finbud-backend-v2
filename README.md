# Springboot JWT Template
Springboot JWT template with PostgreSQL. This repository will be updated along with new versions.

### Dependencies:
- Spring boot 3.0.2
- Spring Web
- Spring Security
- Spring JPA
- io.jsonwebtoken (0.11.5)
- Java 17

### To Do's
1. Change the secret key in the application properties file
   1. Go to https://www.allkeysgenerator.com/
   2. Choose the "Encryption Key" tab
   3. For security level, choose at least the 256-bit
   4. Check the "Hex?" box
   5. Copy it to the application properties
2. Check the user models. By default, this template uses username and not the email.

### Credits
- Amigoscode for the tutorial for this project (https://www.youtube.com/watch?v=KxqlJblhzfI&t=522s&ab_channel=Amigoscode)