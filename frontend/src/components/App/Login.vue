<template>
  <div id="login">
    <h2>Login</h2>
    <el-form :model="credentials" :rules="rules" ref="login-form">
      <el-alert id="authentication-error"
                v-if="invalidCredentials"
                type="error"
                title="Invalid credentials"
                :closable="false" />
      <el-row>
        <el-form-item label="Username" prop="username" :error="getError('username')">
          <el-input v-model="credentials.username" @change="invalidCredentials = false"></el-input>
        </el-form-item>
      </el-row>
      <el-row>
        <el-form-item label="Password" prop="password" :error="getError('password')">
          <el-input type="password" v-model="credentials.password" @change="invalidCredentials = false"></el-input>
        </el-form-item>
      </el-row>
      <el-row type="flex" justify="end">
        <el-button type="primary" :disabled="loginSubmitting" @click="login()">
          <div>Login</div>
          <state-indicator v-if="loginSubmitting" state="processing"
                           id="save-state-indicator"></state-indicator>
        </el-button>
      </el-row>
    </el-form>
  </div>
</template>

<script>
import CommonValidations from '@/components/mixins/CommonValidations'
import StateIndicator from '@/components/App/StateIndicatorComponent'
import AuthenticationError from '@/errors/AuthenticationError'
import auth from '@/utils/auth'

export default {
  name: 'Login',
  components: {StateIndicator},
  mixins: [CommonValidations],

  data () {
    return {
      credentials: {
        username: '',
        password: '',
      },
      loginSubmitting: false,
      invalidCredentials: false,
      rules: {
        username: {required: true, message: 'Please provide an username', trigger: 'blur'},
        password: {required: true, message: 'Please provide a password', trigger: 'blur'},
      }
    }
  },

  methods: {
    login() {
      this.invalidCredentials = false
      this.loginSubmitting = true

      this.$refs['login-form'].validate((isValid) => {
          if (!isValid) {
            this.loginSubmitting = false
            return
          }

          return auth.login(this.credentials)
            .then(() => this.invalidCredentials = false)
            .then(this.authenticationSuccess)
            .catch((error) => {
              this.authenticationError(error)
              this.loginSubmitting = false
            })
        })
    },

    authenticationSuccess() {
      this.$router.go()
    },

    authenticationError(error) {
      if (!(error instanceof AuthenticationError)) { return }

      this.invalidCredentials = true
    }
  }
}
</script>

<style lang="scss" scoped>
#authentication-error {
  margin: 1em 0;
}
</style>
