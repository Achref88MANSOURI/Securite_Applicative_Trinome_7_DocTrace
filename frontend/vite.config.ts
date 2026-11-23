import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import { VitePWA } from 'vite-plugin-pwa'

export default defineConfig({
  plugins: [
    react(),

    VitePWA({
      registerType: 'prompt',
      injectRegister: 'auto',

      manifest: {
        name: 'DocTrace',
        short_name: 'DocTrace',
        description: 'Plateforme de traçabilité et de vérification documentaire',
        theme_color: '#0f172a',
        background_color: '#ffffff',
        display: 'standalone',
        start_url: '.',
        scope: '.',
      },

      workbox: {
        // Précharger uniquement les fichiers statiques de l'application
        globPatterns: ['**/*.{js,css,html}'],

        // Ne pas mettre en cache les réponses API
        runtimeCaching: [],

        // Ne pas traiter les routes API comme des pages React
        navigateFallbackDenylist: [/\/api(?:\/|$)/],

        cleanupOutdatedCaches: true,
      },
    }),
  ],
})
