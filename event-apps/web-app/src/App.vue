<template>
  <div id="app">
    <vue-headful
      title="EventStudio"
      description="Description from the EventStudio"
    />
    <Navigation/>
    <div class="mb-4"/>
    <main class="container">
      <ErrorHandling />
      <router-view/>
    </main>
  </div>
</template>

<script>
import Navigation from './components/Navigation'
import vueHeadful from 'vue-headful'
import ErrorHandling from './components/ErrorHandling'

export default {
  name: 'App',
  components: {ErrorHandling, Navigation, vueHeadful}
}
</script>

<style>
  @import url('https://fonts.googleapis.com/css?family=Roboto');
  #app {
    /*font-family: 'Avenir', Helvetica, Arial, sans-serif;*/
    font-family: 'Roboto', sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    color: #2c3e50;
  }
</style>
