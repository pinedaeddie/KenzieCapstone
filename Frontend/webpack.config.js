const path = require('path');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const CopyPlugin = require("copy-webpack-plugin");
const { CleanWebpackPlugin } = require('clean-webpack-plugin');

module.exports = {
  optimization: {
    usedExports: true
  },
  entry: {
    mainPage: path.resolve(__dirname, 'src', 'pages', 'mainPage.js'),
    scheduleAppointmentPage: path.resolve(__dirname, 'src', 'pages', 'scheduleAppointmentPage.js'),
    updateAppointmentPage: path.resolve(__dirname, 'src', 'pages', 'updateAppointmentPage.js'),
    deleteAppointmentPage: path.resolve(__dirname, 'src', 'pages', 'deleteAppointmentPage.js'),
    appointmentHistoryPage: path.resolve(__dirname, 'src', 'pages', 'appointmentHistoryPage.js'),
    //examplePage: path.resolve(__dirname, 'src', 'pages', 'examplePage.js'),
  },
  output: {
    path: path.resolve(__dirname, 'dist'),
    filename: '[name].js',
  },
  devServer: {
    https: false,
    port: 8080,
    open: true,
    proxy: [
      {
        context: [
          '/appointments',
        ],
        target: 'http://localhost:5001'
      }
    ]
  },
  plugins: [
    new HtmlWebpackPlugin({
      template: './src/main.html',
      filename: 'main.html',
      inject: false
    }),
    new HtmlWebpackPlugin({
      template: './src/index.html',
      filename: 'index.html',
      inject: false
    }),
    new CopyPlugin({
      patterns: [
        {
          from: path.resolve('src/css'),
          to: path.resolve("dist/css")
        }
      ]
    }),
    new CopyPlugin({
      patterns: [
        {
          from: path.resolve('src/image'),
          to: path.resolve("dist/image")
        }
      ]
    }),
    new CleanWebpackPlugin()
  ]
}
