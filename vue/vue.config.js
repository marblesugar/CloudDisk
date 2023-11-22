const { defineConfig } = require('@vue/cli-service')
const webpack = require('webpack')
module.exports = defineConfig({
    transpileDependencies: true,
    chainWebpack: (config) => {
        config.plugin("define").tap((args) => {
            args[0]["process"] = { ...args[0]["process.env"] }
            return args;
        });
    }
})
