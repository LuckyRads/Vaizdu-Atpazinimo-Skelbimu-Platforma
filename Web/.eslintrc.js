module.exports = {
    env: {
        browser: true,
    },
    root: true,
    extends: [
        'plugin:react/recommended',
        'airbnb',
        'airbnb-typescript/base',
        'eslint:recommended',
        'plugin:@typescript-eslint/recommended',
        'plugin:@typescript-eslint/eslint-recommended',
        'prettier',
        'prettier/prettier',
        'plugin:prettier/recommended',
        'typestrict',
    ],
    plugins: [
        'react',
        '@typescript-eslint',
        'import',
        'prettier',
        'react-hooks',
    ],
    parser: '@typescript-eslint/parser',
    parserOptions: {
        project: './tsconfig.json',
        tsconfigRootDir: __dirname,
    },
    settings: {
        'import/resolver': {
            typescript: {},
        },
        react: {
            version: 'detect',
        },
    },
    rules: {
        '@typescript-eslint/no-use-before-define': ['error'],
        'react/jsx-filename-extension': [
            'warn',
            {
                extensions: ['.tsx'],
            },
        ],
        'import/extensions': [
            'error',
            'ignorePackages',
            {
                ts: 'never',
                tsx: 'never',
            },
        ],
        '@typescript-eslint/no-shadow': ['error'],
        'react-hooks/rules-of-hooks': 'error',
        'react-hooks/exhaustive-deps': 'warn',
        quotes: [
            'error',
            'single',
            {
                avoidEscape: true,
            },
        ],
        semi: ['error', 'always'],
        'prettier/prettier': 'error',
        'import/no-extraneous-dependencies': [
            'error',
            {
                devDependencies: false,
                optionalDependencies: false,
                peerDependencies: false,
            },
        ],
        'jsx-a11y/href-no-hash': 'off',
        '@typescript-eslint/explicit-function-return-type': ['error'],
    },
};
