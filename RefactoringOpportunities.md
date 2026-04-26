| Local                   | Nome do Cheiro (Code Smell)      | Nome da Refabricação          | Número de Aluno |
|:------------------------|:---------------------------------|:------------------------------|:----------------|
| Game                    | Large Class                      | Extract Class                 | 1               |
| Game::jsonString        | Redundant assignment             | Inline Variable               | 1               |
| Game::(método complexo) | Long Method                      | Extract Method                | 1               |
| Game                    | Speculative Generality           | Safe Delete                   | 1               |
| Game                    | Long Parameter List              | Change Signature              | 1               |
| Ship                    | Large Class                      | Extract Class                 | 2               |
| Ship                    | Switch Statements                | Decompose Conditional         | 2               |
| Ship                    | Data Class                       | Encapsulate Field             | 2               |
| Ship::(constantes)      | Expressions & Conditionals       | Introduce Constant            | 2               |
| Ship::(método longo)    | Long Method                      | Extract Method                | 2               |
| Move                    | Large Class                      | Extract Method                | 3               |
| Position                | Large Class                      | Introduce Parameter Object    | 3               |
| Position                | Encapsulation & API Design       | Introduce Getter/Setter       | 3               |
| Move::(lógica complexa) | Expressions & Conditionals       | Introduce Variable            | 3               |
| Move::(variáveis)       | Poor responsibility              | Rename                        | 3               |
| Fleet                   | Poor responsibility distribution | Move Method                   | 123008          |
| Tasks                   | Long Method                      | Extract Method                | 123008          |
| Fleet                   | Refused Bequest                  | Pull Up Method                | 123008          |
| Fleet                   | Poor responsibility (Naming)                    | Rename                              | 123008          |
| Tasks                   | Inappropriate Intimacy (Visibility)                                 | Change Visibility             | 123008          |