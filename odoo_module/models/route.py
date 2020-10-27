# -*- coding: utf-8 -*-
from openerp import models, fields, api

class route(models.Model):
    _name = 'batoi_logic.route'
    deliveryman_id = fields.Many2one('batoi_logic.delivery_man', 'Deliveryman with this route', required=True)
    delivery_notes_id = fields.One2many('batoi_logic.delivery_note', 'route_id', 'Delivery Notes associated')
