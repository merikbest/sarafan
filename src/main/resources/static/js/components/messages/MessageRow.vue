<template>
    <v-card class="my-2">
        <v-card-text primary-title>
            <i>({{ message.id }})</i>
            {{ message.text }}
        </v-card-text>
        <media v-if="message.link" :message="message"></media>
        <v-card-actions>
            <v-btn value="Edit" @click="edit" small text rounded>Edit</v-btn>
            <v-btn icon @click="del" small>
                <v-icon>{{ deleteIcon }}</v-icon>
            </v-btn>
        </v-card-actions>
    </v-card>
</template>

<script>
    import {mdiDeleteOutline} from '@mdi/js'
    import {mapActions} from 'vuex'
    import Media from 'components/media/Media.vue';

    export default {
        props: ['message', 'editMessage'],
        components: { Media },
        data() {
            return {
                deleteIcon: mdiDeleteOutline
            }
        },
        methods: {
            ...mapActions(['removeMessageAction']),
            edit() {
                this.editMessage(this.message)
            },
            del() {
                this.removeMessageAction(this.message)
            }
        }
    }
</script>

<style>
</style>