<template>
    <v-card class="my-2">
        <v-card-text primary-title>
            <i>({{ message.id }})</i>
            {{ message.text }}
        </v-card-text>
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

    export default {
        props: ['message', 'editMessage', 'deleteMessage', 'messages'],
        data() {
            return {
                deleteIcon: mdiDeleteOutline
            }
        },
        methods: {
            edit() {
                this.editMessage(this.message)
            },
            del() {
                this.deleteMessage(this.message)
            }
        }
    }
</script>

<style>
</style>