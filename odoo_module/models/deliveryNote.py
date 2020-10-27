# -*- coding: utf-8 -*-

from odoo import models, fields, api

class deliveryNote(models.Model):
    _name = 'batoi_logic.delivery_note'
    notes = fields.Text('Notes')
    order_id = fields.Many2one('batoi_logic.order', 'Associated Order', required=True)
    route_id = fields.Many2one('batoi_logic.route', 'Route')

    # One2one -->
    _sql_constraints = [
        ('one2One_deliveryNote', 'unique("order_id")', 'Order must be unique, because is a One2one relationship!'),
    ]
