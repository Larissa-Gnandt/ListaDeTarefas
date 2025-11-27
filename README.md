# Lista de Tarefas

Aplicativo Android para Wear OS (relógio) com funcionalidades de lista de tarefas e reprodução de áudio via Text-to-Speech (TTS).

## Descrição

Aplicativo desenvolvido para Android Wear OS que permite gerenciar uma lista de tarefas diretamente no relógio. O aplicativo inclui funcionalidades avançadas de áudio, incluindo Text-to-Speech para leitura de mensagens e notificações, além de gerenciamento de dispositivos de áudio (alto-falante e fones Bluetooth).

## Funcionalidades

- **Lista de Tarefas**: Interface para visualização de tarefas (em desenvolvimento)
- **Text-to-Speech (TTS)**: Reprodução de texto em áudio com suporte a múltiplos idiomas
- **Gerenciamento de Áudio**: Detecção e gerenciamento de dispositivos de áudio (alto-falante e Bluetooth)
- **Notificações por Áudio**: Leitura automática de mensagens e notificações
- **Configuração Bluetooth**: Acesso rápido às configurações Bluetooth do dispositivo

## Tecnologias

- **Linguagem**: Java
- **Plataforma**: Android Wear OS
- **SDK**: Android SDK 30+
- **Target SDK**: 36
- **Build System**: Gradle (Kotlin DSL)
- **Bibliotecas**:
  - AndroidX AppCompat
  - Material Design Components
  - AndroidX Activity
  - ConstraintLayout

## Requisitos

- Android Studio (versão mais recente recomendada)
- Android SDK 30 ou superior
- Target SDK 36
- Dispositivo Wear OS ou emulador Wear OS
- **Nota**: O TTS pode não funcionar corretamente em emuladores. Teste em dispositivo real para melhor experiência.

## Permissões

O aplicativo requer as seguintes permissões:

- `BODY_SENSORS`: Para sensores do dispositivo
- `WAKE_LOCK`: Para manter o dispositivo ativo durante operações
- `BLUETOOTH`: Para comunicação Bluetooth
- `BLUETOOTH_ADMIN`: Para gerenciamento de conexões Bluetooth
- `BLUETOOTH_CONNECT`: Para conexão com dispositivos Bluetooth (Android 12+)

## Como Executar

1. Clone o repositório:
```bash
git clone <url-do-repositorio>
cd ListaDeTarefas
```

2. Abra o projeto no Android Studio

3. Configure um dispositivo Wear OS:
   - Conecte um relógio Wear OS via USB ou
   - Use um emulador Wear OS (Android Studio > Tools > Device Manager)

4. Execute o aplicativo:
   - Clique em "Run" (Shift+F10) ou
   - Use o menu Run > Run 'app'

## Estrutura do Projeto

```
ListaDeTarefas/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/listadetarefas/
│   │   │   │   ├── MainActivity.java          # Activity principal
│   │   │   │   ├── AudioPlayer.java           # Gerenciador de TTS
│   │   │   │   ├── AudioDeviceManager.java    # Gerenciador de dispositivos de áudio
│   │   │   │   ├── AudioHelper.java           # Utilitários de áudio
│   │   │   │   └── BluetoothHelper.java       # Utilitários Bluetooth
│   │   │   ├── res/
│   │   │   │   ├── layout/                    # Layouts XML
│   │   │   │   ├── values/                    # Strings, cores, temas
│   │   │   │   └── drawable/                  # Recursos gráficos
│   │   │   └── AndroidManifest.xml
│   │   ├── androidTest/                       # Testes de instrumentação
│   │   └── test/                              # Testes unitários
│   └── build.gradle.kts
├── gradle/
│   └── libs.versions.toml                     # Versões de dependências
├── build.gradle.kts                           # Build do projeto raiz
└── settings.gradle.kts
```

## Componentes Principais

### MainActivity
Activity principal que gerencia a interface do usuário, inicializa os componentes de áudio e gerencia os listeners de eventos.

### AudioPlayer
Classe responsável pela inicialização e gerenciamento do Text-to-Speech, incluindo suporte a múltiplos idiomas e retry automático em caso de falha na inicialização.

### AudioDeviceManager
Gerencia a detecção e monitoramento de dispositivos de áudio conectados, incluindo alto-falante e fones Bluetooth.

### AudioHelper
Utilitário para verificação de disponibilidade de dispositivos de áudio e gerenciamento do AudioManager.

### BluetoothHelper
Utilitário para abertura de configurações Bluetooth do sistema.

## Características do Wear OS

- **Standalone App**: O aplicativo funciona de forma independente, sem necessidade de um smartphone pareado
- **Otimizado para Relógio**: Interface adaptada para telas pequenas e interação por toque
- **Tema Escuro**: Interface com tema escuro otimizado para displays OLED

## Desenvolvimento

### Build

```bash
./gradlew build
```

### Instalação no dispositivo

```bash
./gradlew installDebug
```

### Limpeza do projeto

```bash
./gradlew clean
```

## Notas de Desenvolvimento

- O TTS pode levar alguns segundos para inicializar. O aplicativo implementa retry automático com até 3 tentativas
- Em emuladores, o TTS pode não estar disponível. Teste em dispositivo real para funcionalidade completa
- O aplicativo detecta automaticamente mudanças nos dispositivos de áudio conectados
- Suporte a múltiplos idiomas: Português (Brasil), Português, Inglês (US)

## Versão

- **Version Code**: 1
- **Version Name**: 1.0

