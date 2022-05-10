const scanner = require('sonarqube-scanner');

scanner(
    {
        serverUrl: 'http://localhost:9000',
        login: 'admin',
        password: 'P@ssword123',
        options: {
            'sonar.sources': './src',
            'sonar.login': 'admin',
            'sonar.password': 'P@ssword123',
            'sonar.projectKey': 'Smart-Ad-Platform-Web',
            'sonar.projectName': 'Smart-Ad-Platform-Web',
        },
    },
    () => process.exit()
);
