module.exports = function (api) {
  api.cache(true);
  return {
    presets: ['babel-preset-expo'],
    plugins: [
      [
        'module-resolver',
        {
          root: ['./src'],
          alias: {
            '@src': './src',
            '@components': './src/components',
            '@constants': './src/constants',
            '@models': './src/models',
            '@pages': './src/pages',
            '@contexts': './src/contexts',
            '@services': './src/services',
            '@native': './src/native-modules',
            '@assets': './assets',
          },
        },
      ],
    ],
  };
};
