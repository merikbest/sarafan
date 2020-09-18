<template>
    <v-list-item>
        <v-list-item-content>
            <v-list-item-title>{{comment.text}}</v-list-item-title>
        </v-list-item-content>
    </v-list-item>
</template>

<script>
    export default {
        name: "CommentItem",
        props: ['comment']
    }
</script>

<style scoped>

</style>