/*
 * Copyright 2014-2024 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

const { join, resolve } = require('path');

const ringUiWebpackConfig = require('@jetbrains/ring-ui/webpack.config');
const MiniCssExtractPlugin = require('mini-css-extract-plugin');
const { BundleAnalyzerPlugin } = require('webpack-bundle-analyzer');
const WebpackShellPluginNext = require('webpack-shell-plugin-next');
const TerserPlugin = require('terser-webpack-plugin');

const pkgConfig = require('./package.json').config;

const componentsPath = join(__dirname, pkgConfig.components);

const webpackConfig = (params, argv) => {
  const mode = argv.mode || 'development';
  const isAnalyze = argv.env && argv.env.analyze === 'true';
  return {
    entry: `${componentsPath}/root.tsx`,
    resolve: {
      mainFields: ['module', 'browser', 'main'],
      extensions: ['.tsx', '.ts', '.js', '.svg'],
      alias: {
        react: resolve('./node_modules/react'),
        'react-dom': resolve('./node_modules/react-dom'),
        '@jetbrains/ring-ui': resolve('./node_modules/@jetbrains/ring-ui'),
      },
    },
    module: {
      rules: [
        ...ringUiWebpackConfig.config.module.rules,
        {
          test: /\.s[ac]ss$/i,
          use: [MiniCssExtractPlugin.loader, 'css-loader', 'sass-loader'],
          include: componentsPath,
          exclude: ringUiWebpackConfig.componentsPath,
        },
        {
          test: /\.tsx?$/,
          use: [
            {
              loader: 'ts-loader',
              options: {
                transpileOnly: true,
              },
            },
          ],
        },
        {
          test: /\.svg$/,
          loader: require.resolve('svg-inline-loader'),
          options: { removeSVGTagAttrs: false },
          include: [require('@jetbrains/icons')],
        },
      ],
    },
    plugins: [
      new MiniCssExtractPlugin({
        // Options similar to the same options in webpackOptions.output
        // both options are optional
        filename: '[name].css',
        chunkFilename: '[id].css',
      }),
      ...(mode === 'development'
        ? [
            new WebpackShellPluginNext({
              onAfterDone: {
                scripts: [
                  'echo "[search] Done rebuild, coping files to dokka-integration-tests"',
                  'cp ./dist/main.js ../../dokka-integration-tests/gradle/build/ui-showcase-result/scripts',
                  'cp ./dist/main.css ../../dokka-integration-tests/gradle/build/ui-showcase-result/styles',
                ],
                blocking: false,
                parallel: true,
              },
            }),
          ]
        : []),
      ...(isAnalyze ? [new BundleAnalyzerPlugin()] : []),
    ],
    optimization: {
      minimize: true,
      minimizer: [
        new TerserPlugin({
          extractComments: false,
          ...(mode === 'development'
            ? {
                terserOptions: {
                  keep_classnames: true,
                  keep_fnames: true,
                },
              }
            : {}),
        }),
      ],
    },
    output: {
      path: __dirname + '/dist/',
    },
  };
};

module.exports = webpackConfig;
