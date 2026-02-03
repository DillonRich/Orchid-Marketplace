import { PublicClientApplication, Configuration, LogLevel } from '@azure/msal-browser'

// Azure AD B2C Configuration
// These values should be set in your .env.local file
const azureConfig = {
  tenantName: process.env.NEXT_PUBLIC_AZURE_AD_B2C_TENANT_NAME || '',
  clientId: process.env.NEXT_PUBLIC_AZURE_AD_B2C_CLIENT_ID || '',
  signInPolicy: process.env.NEXT_PUBLIC_AZURE_AD_B2C_SIGNIN_POLICY || 'B2C_1_signupsignin1',
  redirectUri: process.env.NEXT_PUBLIC_AZURE_AD_B2C_REDIRECT_URI || 'http://localhost:3000',
}

// Check if Azure AD B2C is configured
export const isAzureB2CConfigured = () => {
  return !!(azureConfig.tenantName && azureConfig.clientId)
}

// MSAL Configuration
const msalConfig: Configuration = {
  auth: {
    clientId: azureConfig.clientId,
    authority: `https://${azureConfig.tenantName}.b2clogin.com/${azureConfig.tenantName}.onmicrosoft.com/${azureConfig.signInPolicy}`,
    knownAuthorities: [`${azureConfig.tenantName}.b2clogin.com`],
    redirectUri: azureConfig.redirectUri,
    postLogoutRedirectUri: azureConfig.redirectUri,
  },
  cache: {
    cacheLocation: 'localStorage',
  },
  system: {
    loggerOptions: {
      loggerCallback: (level, message, containsPii) => {
        if (containsPii) return
        switch (level) {
          case LogLevel.Error:
            console.error(message)
            break
          case LogLevel.Info:
            console.info(message)
            break
          case LogLevel.Verbose:
            console.debug(message)
            break
          case LogLevel.Warning:
            console.warn(message)
            break
        }
      },
    },
  },
}

// Create MSAL instance only if configured
let msalInstance: PublicClientApplication | null = null

if (isAzureB2CConfigured()) {
  msalInstance = new PublicClientApplication(msalConfig)
}

export const getMsalInstance = (): PublicClientApplication | null => {
  return msalInstance
}

// Login request configuration
export const loginRequest = {
  scopes: ['openid', 'profile', 'email'],
}

// Helper functions for Azure AD B2C authentication
export const azureB2CLogin = async () => {
  if (!msalInstance) {
    throw new Error('Azure AD B2C is not configured')
  }

  try {
    const response = await msalInstance.loginPopup(loginRequest)
    return response
  } catch (error) {
    console.error('Azure B2C login error:', error)
    throw error
  }
}

export const azureB2CLogout = async () => {
  if (!msalInstance) {
    throw new Error('Azure AD B2C is not configured')
  }

  try {
    await msalInstance.logoutPopup({
      mainWindowRedirectUri: azureConfig.redirectUri,
    })
  } catch (error) {
    console.error('Azure B2C logout error:', error)
    throw error
  }
}

export const getAzureB2CAccount = () => {
  if (!msalInstance) return null

  const accounts = msalInstance.getAllAccounts()
  return accounts.length > 0 ? accounts[0] : null
}

export const getAzureB2CToken = async () => {
  if (!msalInstance) return null

  const account = getAzureB2CAccount()
  if (!account) return null

  try {
    const response = await msalInstance.acquireTokenSilent({
      ...loginRequest,
      account,
    })
    return response.accessToken
  } catch (error) {
    console.error('Error acquiring token:', error)
    // If silent acquisition fails, try interactive
    try {
      const response = await msalInstance.acquireTokenPopup(loginRequest)
      return response.accessToken
    } catch (err) {
      console.error('Error with popup token acquisition:', err)
      return null
    }
  }
}
