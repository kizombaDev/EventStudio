<template>
  <div id="app">
    <Navigation/>
    <div class="mb-4"/>
    <main class="container">
      <router-view/>
    </main>
  </div>
</template>

<script>
import Navigation from './components/Navigation'
export default {
  name: 'App',
  components: {Navigation}
}
</script>

<style>
  @import url('https://fonts.googleapis.com/css?family=Roboto');
  #app {
    /*font-family: 'Avenir', Helvetica, Arial, sans-serif;*/
    font-family: 'Roboto', sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    text-align: center;
    color: #2c3e50;
  }
</style>
